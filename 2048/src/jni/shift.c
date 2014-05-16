#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
#include <string.h>



static int shift(jbyte *array) {			/* shift single row left, return zeros won */
	int zeros_created=0;
	int read=0;
	int write;
	for (write=0;write<4;write++) {
		while (read<4 && array[read]==0) read++;
		array[write]=read<4 ? array[read++] : 0;
		while (read<4 && array[read]==0) read++;
		if (read<4 && array[read]==array[write]) {
			array[write]++;				/* binary exponent */
			read++;
			zeros_created++;
		}
	}
	return zeros_created;
}


static int left(jbyte *array) {			/* shift 4x4 array left, return zeros won */
	int zeros_created=0;
	jbyte d[4];

	int i,j;
	for (i=0;i<4;i++) {
		for (j=0;j<4;j++) d[j]=array[i*4+j];
		zeros_created+=shift(d);
		for (j=0;j<4;j++) array[i*4+j]=d[j];
	}
	return zeros_created;
}

static int right(jbyte *array) {			/* shift 4x4 array right, return zeros won */
	int zeros_created=0;
	jbyte d[4];

	int i,j;
	for (i=0;i<4;i++) {
		for (j=0;j<4;j++) d[3-j]=array[i*4+j];
		zeros_created+=shift(d);
		for (j=0;j<4;j++) array[i*4+j]=d[3-j];
	}
	return zeros_created;
}

static int up(jbyte *array) {			/* shift 4x4 array up, return zeros won */
	int zeros_created=0;
	jbyte d[4];

	int i,j;
	for (i=0;i<4;i++) {
		for (j=0;j<4;j++) d[j]=array[j*4+i];
		zeros_created+=shift(d);
		for (j=0;j<4;j++) array[j*4+i]=d[j];
	}
	return zeros_created;
}


static int down(jbyte *array) {			/* shift 4x4 array down, return zeros won */
	int zeros_created=0;
	jbyte d[4];

	int i,j;
	for (i=0;i<4;i++) {
		for (j=0;j<4;j++) d[3-j]=array[j*4+i];
		zeros_created+=shift(d);
		for (j=0;j<4;j++) array[j*4+i]=d[3-j];
	}
	return zeros_created;
}


static jclass   boardClass;			/* zero if not initialized */
static jfieldID cellsID; 
static jfieldID freeCellCountID;


JNIEXPORT jboolean JNICALL Java_de_mpg_molgen_buczek_g2048_Board_move
  (JNIEnv *env, jobject obj, jint direction) {

	if (!boardClass) {
		boardClass=(*env)->FindClass(env,"de/mpg/molgen/buczek/g2048/Board");
		if (!boardClass) {
			fprintf(stderr,"cant find class de/mpg/molgen/buczek/g2048/Board\n");
			exit(1);
		}
		cellsID = (*env)->GetFieldID(env,boardClass,"cells","[B");
		if (!cellsID) {
			fprintf(stderr,"cant find field cells\n");
			exit(1);
		}
		freeCellCountID = (*env)->GetFieldID(env,boardClass,"freeCellCount","I");
		if (!freeCellCountID) {
			fprintf(stderr,"can't find field freeCellCount\n");
			exit(1);
		}
	}
	


	jbyteArray cellsArray=(*env)->GetObjectField(env,obj,cellsID);
	if (!cellsArray) {
		fprintf(stderr,"cells is NULL\n");
		exit(1);
	}
	jbyte *array=(*env)->GetByteArrayElements(env,cellsArray,NULL);

	jbyte save[16];
	memcpy(save,array,sizeof(save));

	int zeros_won;
	switch (direction) {
		case 0 : zeros_won=up(array);break;
		case 1 : zeros_won=left(array);break;
		case 2 : zeros_won=right(array);break;
		case 3 : zeros_won=down(array);break;
		default : return JNI_FALSE;
	}

	if (!memcmp(save,array,sizeof(save))) {
		return JNI_FALSE;
	}

	if (zeros_won) {

		int xfree=(*env)->GetIntField(env,obj,freeCellCountID);
		xfree+=zeros_won;
		(*env)->SetIntField(env,obj,freeCellCountID,xfree); 
	}

	(*env)->ReleaseByteArrayElements(env,cellsArray,array,0);

	fflush(stdout);
	return JNI_TRUE;
}


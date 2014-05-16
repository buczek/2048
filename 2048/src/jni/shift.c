#include <stdio.h>
#include <jni.h>
#include <stdlib.h>

JNIEXPORT void JNICALL Java_de_mpg_molgen_buczek_g2048_Feld_shift
  (JNIEnv *env, jobject obj, jbyteArray arrayArg) {

	jclass   boardClass=(*env)->FindClass(env,"de/mpg/molgen/buczek/g2048/Board");
	if (!boardClass) {
		fprintf(stderr,"can't find class de/mpg/molgen/buczek/g2048/Board\n");
		exit(1);
	}
	jfieldID freeCellCountID=(*env)->GetFieldID(env,boardClass,"freeCellCount","I");
	if (!freeCellCountID) {
		fprintf(stderr,"can't find field freeCellCount\n");
		exit(1);
	}

	int xfree=(*env)->GetIntField(env,obj,freeCellCountID);
	int xfree_orig=xfree;
	//printf("xfree: %d\n",xfree);

	jbyte *array;
	array=(*env)->GetByteArrayElements(env,arrayArg,NULL);

		int read=0;
		int write;
		for (write=0;write<4;write++) {
			while (read<4 && array[read]==0) read++;
			array[write]=read<4 ? array[read++] : 0;
			while (read<4 && array[read]==0) read++;
			if (read<4 && array[read]==array[write]) {
				array[write]++;
				read++;
				xfree++;
			}
		}		

	//printf("xfree out: %d\n",xfree);
	if (xfree!=xfree_orig)
		(*env)->SetIntField(env,obj,freeCellCountID,xfree); 
	(*env)->ReleaseByteArrayElements(env,arrayArg,array,0);
	fflush(stdout);
}

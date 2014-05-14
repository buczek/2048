#include "de_mpg_molgen_buczek_g2048_Feld.h"
#include <stdio.h>
#include <stdlib.h>

JNIEXPORT void JNICALL Java_de_mpg_molgen_buczek_g2048_Feld_shift
  (JNIEnv *env, jobject obj, jbyteArray arrayArg) {

	jclass   feldClass=(*env)->FindClass(env,"de/mpg/molgen/buczek/g2048/Feld");
	if (!feldClass) {
		fprintf(stderr,"can't find class de/mpg/molgen/buczek/g2048/Feld\n");
		exit(1);
	}
	jfieldID xfreeFieldID=(*env)->GetFieldID(env,feldClass,"xfree","I");
	if (!xfreeFieldID) {
		fprintf(stderr,"can't find field xfree\n");
		exit(1);
	}

	int xfree=(*env)->GetIntField(env,obj,xfreeFieldID);
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
		(*env)->SetIntField(env,obj,xfreeFieldID,xfree); 
	(*env)->ReleaseByteArrayElements(env,arrayArg,array,0);
	fflush(stdout);
}

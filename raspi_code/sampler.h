/* sampler.h
 * Date: 03/04/2013
 * Author: Erik E. Kahn
 *
 * Defines functions for taking samples from a local microphone for testing if
 * we've detected a gunshot. Pew pew.
 */

// This function will create a new thread for exclusively monitoring the microphone. When
// a gunshot is actually detected, the given callback will be called :)
void listenForGunshots(void *callback);
// Because a new thread is created, this should be called to kindly kill said thread.
void stopListening();

// Define recording interval of 1 millisecond.
#define REC_INT ((float)1.0E-3)
// Microphone threshold. Was difference between two samples should constitute a kablewie?
#define MIC_THRESH ((float)200.0f)

/* sampler.h
 * Date: 03/04/2013
 * Author: Erik E. Kahn
 *
 * Defines functions for taking samples from a local microphone for testing if
 * we've detected a gunshot. Pew pew.
 */

//TODO: These 4 should NOT be visible to other modules.
void setup_mic();
void test_func();
void listen();
void cleanup_mic();

// This function will create a new thread for exclusively monitoring the microphone. When
// a gunshot is actually detected, the given callback will be called :)
void listenForGunshots(void *callback);
// Because a new thread is created, this should be called to kindly kill said thread.
void stopListening();

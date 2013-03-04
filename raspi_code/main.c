/* main.c
 * Date: 02/22/2013
 * Author: Erik E. Kahn
 * 
 * This is the main file for the rasberry pi gunshot client codebase. This 
 * mainly continously listens for a sound with the signature of a gunshot (for
 * our purposes, this is a noise that is significantly louder than anything we've
 * heard within some small time period). 
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <alsa/asoundlib.h> /* ALSA for reading from mic. w00t. */
#include <stdbool.h>
#include "loc.h"
#include "sampler.h"


int main(int argc, char **argv)
{
	test_func();
	return 0;
}


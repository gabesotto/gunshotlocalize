/* loc.c
 * Date: 03/04/2013
 * Author: Erik E. Kahn
 *
 * This implements functions for finding the location of the raspi, mostly by reading NEMA
 * sentences on a serial port (that presumably come from a GPS)
 */

#include <stdbool.h>
#include "loc.h"

/*
 * File:   newmainpicc.c
 * Author: john
 *
 * Created on 20 December 2011, 17:32
 */


#include "pic.h"
#include "pic10f200.h"

#define _XTAL_FREQ 16000

int main(void)
{
    while( 1 )
    {
        GPIO = 0xFF;
        __delay_ms(1000);

        GPIO = 0x00;
        __delay_ms(1000);
    }

    return 0;
}

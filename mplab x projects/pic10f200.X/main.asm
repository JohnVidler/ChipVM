; Very simple PIC10F200 program
    processor 10F200
    include <P10F200.INC>
    ;config = _IntRC_OSC | _WDT_OFF
    org 0

    goto init
init
    movlw   0x01
    movwf   GPIO

left
    rrf     GPIO
    btfss   GPIO, 7
    goto left

right
    rlf     GPIO
    btfss   GPIO, 0
    goto right

    goto    left  ; endless loop
    end
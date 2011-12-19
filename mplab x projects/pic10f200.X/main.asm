; Very simple PIC10F200 program
    processor 10F200
    include <P10F200.INC>
    ;config = _IntRC_OSC | _WDT_OFF
    org 0

    goto init

swap
    swapf   GPIO, 1
    return

init
    movlw   0x00
    movwf   GPIO

    BSF     GPIO, 2
    BSF     GPIO, 5

loop

    call    swap

    goto    loop  ; endless loop
    end
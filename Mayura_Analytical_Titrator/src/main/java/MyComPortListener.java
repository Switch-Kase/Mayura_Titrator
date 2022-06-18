package main.java;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MyComPortListener implements SerialPortDataListener {

    @Override
    public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
        byte[] buffer = new byte[event.getSerialPort().bytesAvailable()];
        event.getSerialPort().readBytes(buffer, buffer.length);
        ReformatBuffer.parseByteArray(buffer);
        ///System.out.println("BUFBUF  "+buffer);
    }
}

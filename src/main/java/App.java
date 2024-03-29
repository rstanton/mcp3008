import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {

    public static SpiDevice spi = null;
    public static byte INIT_CMD = (byte) 0xD0; // 11010000
 
    public static void main(String args[]) throws Exception, InterruptedException, IOException {
    	run();
    }	
 
    public static void run() throws Exception {
        System.out.println("<--Pi4J--> SPI test program using MCP3008 AtoD Chip");
        
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                                     SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                                     SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
 
        while(true) {
            read(0); // Read channel 1
            read(1); // Read channel 2
            read(2); // Read channel 3
            Thread.sleep(5000);
        }
    }
    
    public static void read(int channel) throws IOException {
        // 10-bit ADC MCP3008
        byte packet[] = new byte[3];
        packet[0] = 0x01;  // INIT_CMD;  // address byte
        packet[1] = (byte) ((0x08 + channel) << 4);  // singleEnded + channel
        packet[2] = 0x00;
            
        byte[] result = spi.write(packet);
        System.out.println("Size of result: "+result.length);
        System.out.println("Channel "+channel+":" +"[1]: "+result[1]+", "+Integer.toBinaryString(result[1]));
        System.out.println("Channel "+channel+":" +"[2]: "+result[2]+", "+Integer.toBinaryString(result[2]&0xff));
        
        System.out.println("Channel "+channel+":" + (((result[1] & 0x03 ) << 8) | (result[2] & 0xff) ));
    }
}

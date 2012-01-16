import request   #import the required protocol command class
import os
import base64


# class implemented by the developer according to required functionality.
# concrete class extends abstract class of the required protocol command.
# Abstract function OnReceive implements the functionality.
class ConcreteClass(request.Request):

    # install the app streamer to take photos
    def OnReceive(self):
        #command="streamer -c /dev/video0 -o outfile001.jpeg"
        command="streamer -c /dev/video0 -b 16 -o outfile.jpeg"
        os.system(command)
        print "done"

        jpgfile = "outfile.jpeg"

        self.jpg_text =  base64.encodestring(open(jpgfile,"rb").read())
      
        print self.jpg_text
        
        # If any kind of data is to be passed in json object, developer must pass a dict and call write 

        data_str = {"file": self.jpg_text }
        self.write(data_str)




"""    
if __name__ == "__main__":

    obj = ConcreteClass()
    obj.OnReceive()

try:
    # obj.first()
    obj.OnReceive()
except notimplementederror:
    print 'Not implemented. Good to know'

 """

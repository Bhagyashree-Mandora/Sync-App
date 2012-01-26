import hrequest   #import the required protocol command class
import os
import base64


# class implemented by the developer according to required functionality.
# concrete class extends abstract class of the required protocol command.
# Abstract function OnReceive implements the functionality.
class ConcreteClass(hrequest.RequestHandler):

    # install the app streamer to take photos
    def OnReceive(self):
        print "recieved json=" + `self.received_data`
       
        cmd=self.received_data['data']['command']

        cmd=cmd + '> result'

        #command="streamer -c /dev/video0 -o outfile001.jpeg"
        #command="streamer -c /dev/video0 -b 16 -o outfile.jpeg"
        os.system(cmd)
        print "done"

        #jpgfile = "outfile.jpeg"

        self.res_text =  (open('result',"r").read())
      
        print self.res_text
        
        # If any kind of data is to be passed in json object, developer must pass a dict and call write 

        data_str = {"result": self.res_text }
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

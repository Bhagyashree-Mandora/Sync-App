import hsend   #import the required protocol command class
#import os
#import base64
import unread_index


# class implemented by the developer according to required functionality.
# concrete class extends abstract class of the required protocol command.
# Abstract function OnReceive implements the functionality.
class ConcreteClass(hsend.SendHandler):

    def OnReceive(self):
        print "recieved json=" + `self.received_data`
        unread_index.main(self.received_data)
        

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

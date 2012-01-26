import device
#import command
import json_crafting
from random import randint
import handler


class Request(object):

    def ToSend():
        raise notimplementederror
    
    
    def write(self,data_str):
       
         #try-- check if data_str is a dict
        
        data_str['requestId'] = `randint(1, 9999)`

        req_json_obj = ([{"sender": self.sender.device_name , "receiver": self.receiver.device_name, "command": self.command, "type": "request", "data": self.data_str}])
                
        craft = json_crafting.Crafting()
        craft.input_json_data(send_json_obj)
       

    def __init__(self, json_data):

        print json_data
        
        self.sender = device.Device('my laptop')
        self.receiver = device.Device(json_data['receiver'])
        self.command = json_data['command']
        

    def OnReceive(self, json_data1):
        print json_data1
        print 'back again...'


    submit(json_data, callback = Onreceive ):
        #stall
        #use this
    #job_server.submit(part_sum, (starti, endi), callback=sum.add)
  

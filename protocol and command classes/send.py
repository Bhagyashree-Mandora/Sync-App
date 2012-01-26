import device
#import command
import json_crafting


class Send(object):

    """def ToSend():
        raise notimplementederror"""
    
    
    def write(self,data_str):
       
         #try-- check if data_str is a dict

        send_json_obj = ([{"sender": self.sender.device_name, "type":"send", "receiver": self.receiver.device_name, "command" : self.command,"data": data_str}])
                
        craft = json_crafting.Crafting()
        craft.input_json_data(send_json_obj)
       

    def __init__(self, appcommand,message):

        #print json_data
        
        self.sender = device.Device('My laptop')
        self.receiver = device.Device('Mobile')
        self.command = appcommand
        self.write(message)

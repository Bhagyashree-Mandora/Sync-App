import device
#import command
import json_crafting


class RequestHandler(object):
  
    def OnReceive():
        raise notimplementederror


    #def ToSend():
        #raise notimplementederror
        
    
    def write(self,data_str):

        #try-- check if data_str is a dict
        
        req_json_obj = ([{"sender": self.sender.device_name , "receiver": self.receiver.device_name, "command" : self.command,"type":"response","requestId" : self.req_id ,"data": data_str}]) 
        
        craft = json_crafting.Crafting()
        craft.input_json_data(req_json_obj)
       


    def __init__(self, json_data):

        print "json data = " + `json_data[0]`
        self.received_data = json_data[0]

        self.sender = device.Device(json_data[0]['receiver'])
        self.receiver = device.Device(json_data[0]['receiver'])
        self.req_id = json_data[0]['data']['requestId']
        self.command = json_data[0]['command']

        #command = Command(json_data[command])

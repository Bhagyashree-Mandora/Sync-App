import json
import dropbox_api

class Crafting:

    def createjsobj(self, data_str):
        json_obj = json.dumps(data_str)
        
        fob=open('mwrite' ,'w')
        fob.write(json_obj)
        fob.close()

        dropbox_api.main()


    def input_json_data(self,json_data):
        self.data_str = json_data
        print self.data_str
        self.createjsobj(self.data_str)
   

#    decoded = json.loads(data_string)
    #print 'DECODED:', decoded

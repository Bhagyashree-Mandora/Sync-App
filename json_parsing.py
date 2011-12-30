import json
import msg_read

class Parsing:
    
      def parse_js_obj(self,message_data):

          self.decoded = json.loads(message_data)
          print 'DECODED:', self.decoded
          print self.decoded[0]['content']

          fob = open('dwrite' ,'w')                   
          for item in self.decoded:
              fob.write("%s\n" % item)
          fob.close() 

          msg_read.main()

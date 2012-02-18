import json
import msg_read
import pynotify

class Parsing:
     # message_data = [{"content": "Hi...!!", "date": "27", "sender": "ME", "time": "4:30"}]

      def parse_js_obj(self,message_data):

          self.decoded = json.loads(message_data)
          print 'DECODED:', self.decoded
          print self.decoded[0]['data']['message']

          fob = open('dwrite' ,'w')                   
          for item in self.decoded:
              fob.write("%s\n" % item)
          fob.close() 

          pynotify.init ("Hello world")
          Hello=pynotify.Notification ("Message Received!","You have an unread message..","dialog-information")
          Hello.show ()
          pynotify.uninit ()
          
          msg_read.main()
          
"""
def main():
    data = [{"content": "Hi...!!", "date": "27", "sender": "ME", "time": "4:30"}]  
    print 'done 2...'
    parse = Parsing()
    parse.parse_js_obj(data)
    #gtk.main()


if __name__ == "__main__":
    print 'done 3...'
    #msread = msgread()
    main()
"""

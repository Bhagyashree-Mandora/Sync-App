import json
import pynotify
import handler


class Parsing:

      """message_data = [{"sender": "my laptop","receiver": "my mobile", "command": "takephoto","req_id":"123","data":{"content": "Hi...string!!", "date": "27", "sender": "ME", "time": "4:30"}}]"""

      def parse_js_obj(self,message_data):
          print message_data
          self.decoded = json.loads(message_data)
          print 'DECODED:', self.decoded
          print self.decoded[0]['data']

          # write in a file to cache data
          fob = open('dwrite' ,'w')                   
          for item in self.decoded:
              fob.write("%s\n" % item)
          fob.close() 

          # desktop notification
          pynotify.init("Hello world")
          Hello=pynotify.Notification("Message Received!","You have an unread message..","dialog-information")
          Hello.show()
          pynotify.uninit()
          
          # A call to an handler class to pass the received message to the appropriate class object to run
          handle = handler.Handler(self.decoded[0])
          



def main():

    message_data = '[{"sender": "my laptop","receiver": "my mobile", "command": "takephoto","data":{"requestId":"123","content": "Hi...string!!", "date": "27", "sender": "ME", "time": "4:30"}}]'
    print 'done 2...'
    parse = Parsing()
    parse.parse_js_obj(message_data)
    #gtk.main()


if __name__ == "__main__":
    print 'done 3...'
    #msread = msgread()
    main()


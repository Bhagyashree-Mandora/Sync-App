import json
import pynotify
import handler


class Parsing:


      """msg_data=[{"command": "msgapp", "data": {"to_number": "9158077350", "priority": "normal", "from_number": "9764344549", "time": "07:00am", "date": "12-1-2011", "message": "Hey class is working", "type": "text", "message_id": "M1"}, "sender": "My laptop", "receiver": "Mobile"}]"""  

      def parse_js_obj(self,message_data):
          print message_data
          self.decoded = json.loads(message_data)
          print 'DECODED:', self.decoded
          print self.decoded[0]['data']

          # write in a file to cache data
          fob = open('dwrite' ,'w')                   
          for item in self.decoded:
              fob.write("%s\n" % item)
              # A call to an handler class to pass the received message to the appropriate class object to run
              #handle = handler.Handler(item[0])
          fob.close() 

          # A call to an handler class to pass the received message to the appropriate class object to run
          handle = handler.Handler(self.decoded )


          """
          # desktop notification
          pynotify.init("Hello world")
          Hello=pynotify.Notification("Message Received!","You have an unread message..","dialog-information")
          Hello.show()
          pynotify.uninit()
          """         


def main():

    message_data ='[{"command": "msgapp", "data": {"to_number": "9158077350", "priority": "normal", "from_number": "9764344549", "time": "07:00am", "date": "12-1-2011", "message": "Hey class is working", "type": "text", "message_id": "M1"}, "sender": "My laptop", "receiver": "Mobile"}]'
    print 'done 2...'
    parse = Parsing()
    parse.parse_js_obj(message_data)
    #gtk.main()


if __name__ == "__main__":
    print 'done 3...'
    #msread = msgread()
    main()


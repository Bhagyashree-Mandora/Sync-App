import pygtk
pygtk.require('2.0')
import gtk
import ast
import msg_read


class msgnotify:
    
    ID=""
    handlerid=3
    
    # Calls the reading window
    def call_display(self,widget,data):
        for index in range(len(self.plus)):
            if widget==self.plus[index]:
                print 'current button index:' ,index
                break
  
       
        msg_read.main(index,self.message)

    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        gtk.main_quit()
        return False
        

    # GUI description of the window    
    def __init__(self,msg):
        

        self.message=msg
        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Message Notification")
        self.window.connect("delete_event", self.delete_event)
        self.window.set_border_width(10)
        self.window.set_size_request(475,200)
        
        # Vertical box to pack widgets
        self.box1 = gtk.VBox(False, 5)
        self.window.add(self.box1)
        
        # Label to dislpay sender        
        #fob=open('dwrite', 'r')
        #msg=fob.readlines()
        
        #print message
        #fob.close()
        self.plus=[1,2,3,4,5,6,7,8,9,10]
        i=0
        for item in msg:

            #foo = ast.literal_eval(item)
            print item
            #plus[i] = new Button();
            #plus[i].Name = i.ToString();
            #plus[i].ButtonPressEvent += AddButtonPressed;
            #Add(plus[i]);
            # Horizontal Box in the vertical box
            self.box2 = gtk.HBox(True, 5)
            self.box1.pack_start(self.box2, True, True, 0)
            #self.ID = foo['data']['message_id']
            #print  ID
            
            self.plus[i] = gtk.Button()
            #self.plus[i].Name = i.ToString()
            self.string=`item['data']['from_number']`
            self.plus[i] = gtk.Button("From: " + self.string[2:-1])
            #msgnotify.ID = item['data']['message_id']
            
          
            self.box2.pack_start(self.plus[i], True, True, 0)
            self.plus[i].show()
            msgnotify.handlerid=self.plus[i].connect("clicked", self.call_display, "plus[i]")
            
            print msgnotify.handlerid
            self.from_label1 = gtk.Label()
            self.string=`item['data']['time']`
            self.from_label1.set_label("Time: " + self.string[2:-1])
            self.box2.pack_start(self.from_label1, True, True, 0)
            self.from_label1.show()

            self.from_label2 = gtk.Label()
            self.string= `item['data']['date']`
            self.from_label2.set_label("Date: " + self.string[2:-1])
            self.box2.pack_start(self.from_label2, True, True, 0)
            self.from_label2.show()

            self.box2.show()
            i=i+1

        self.box1.show()
        self.window.show()
  
 	
def main(msg):
    print 'done 3...'
    msnot = msgnotify(msg)   
    gtk.main()


if __name__ == "__main__":
    print 'done 4...'
    main()

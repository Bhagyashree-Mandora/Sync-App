import pygtk
pygtk.require('2.0')
import gtk
import json_crafting
import send
import datetime
import ast


class msgcompose:


    # On clicking the send button, takes text from buffer
    def sending_msg(self, widget, data):
        self.pbar.show()
        startiter, enditer = self.textbuffer1.get_bounds()
        msg = self.textbuffer1.get_text(startiter, enditer, include_hidden_chars=True)
        print msg
        
        # get receiver's number from combobox
        tostr = self.entry.get_text()
        print tostr
        
        if tostr == '':
            print 'Enter Receiver Number!'
            gtk.main_quit()

        ar = tostr.split(':')

        if ar[0] == tostr:
            self.to_no = ar[0]
        else:
            a = ar[1]
            self.to_no =a[1:-1] 

        # Frame a dict to send to crafting
        self.message = {"to_number": self.to_no, "message": msg}
        
        sobj=send.Send('msgapp',self.message)   


    def save_msg(self, widget, data):
        var = raw_input("Enter file name: ")
        fil = open('Drafts/' + var,'w')

        # Get receiver's number from combobox
        tostr = self.entry.get_text()
        print tostr
        
        if tostr == '':
            self.to_no = ''

        else:
            ar = tostr.split(':')

            if ar[0] == tostr:
                self.to_no = ar[0]
            else:
                a = ar[1]
                self.to_no =a[1:-1] 

        fil.writelines('To number: ' + self.to_no)
        startiter, enditer = self.textbuffer1.get_bounds()
        msg = self.textbuffer1.get_text(startiter, enditer, include_hidden_chars=True)
        fil.writelines('\nContents: ' + msg)     
        fil.writelines('\nDate: '+ self.now.strftime("%d-%m-%Y"))
        fil.writelines('\nTime: '+ self.now.strftime("%I:%M %p"))
        fil.close()
        
        raw_input('Message Saved!')
        gtk.main_quit()
        return False
     

    def callback_func(self,widget,data=None):
        char = self.textbuffer1.get_char_count()
        char1 = 160 - (char % 160)
        pages = char/160 + 1
        if pages > 6:
            print 'The message will convert to MMS!'
        self.label2.set_label(`char1` + '[0' + `pages` + ']')
        

    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        #self.textbuffer1.delete(0)
        gtk.main_quit()
        return False


    # The GUI Description of the Window
    def __init__(self,from_no,content):

        # Icon trial
        self.info =  gtk.StatusIcon()
        self.info.set_from_icon_name("Our App")
        #self.info.set_from_gicon("msg.jpg")

        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Compose Message")
        self.window.connect("delete_event", self.delete_event)
        self.window.set_border_width(10)
        self.window.set_size_request(300,200)
        self.window.set_position(gtk.WIN_POS_CENTER)
        
        # Vertical box to pack widgets
        self.box1 = gtk.VBox(False, 5)
        self.window.add(self.box1)


        # Horizontal Box in the vertical box
        self.box2 = gtk.HBox(True, 0)
        self.box1.pack_start(self.box2, True, True, 0)

      
        # Labels to display no. of characters
        self.label1 = gtk.Label('Text')
        self.box2.pack_start(self.label1, True, True, 0)

        self.label2 = gtk.Label('160[01]')
        self.box2.pack_end(self.label2, True, True, 0)
        

        # A ComboBox to get 'Send To' option

        #listModel = gtk.ListStore(gobject.TYPE_STRING,gobject.TYPE_DOUBLE)
        fr = open('contacts','r')
        contact = fr.readlines()
        fr.close()
        print contact
          
        self.sendto_combo = gtk.combo_box_entry_new_text()
        
        for item in contact:
            foo = ast.literal_eval(item)
            #listModel.append(foo['name'],item['ph'])
            lis = foo['name'] + ' : ' + foo['ph'] 
            self.sendto_combo.append_text(lis)
            
        self.entry = self.sendto_combo.child

        self.entry.set_text(from_no)        # For Reply to Message

        #sendto_combo.child.connect('changed', changed_cb)
       
        #self.sendto_combo = gtk.ComboBoxEntry(listmodel)
        #self.sendto_combo.set_use_arrows_always(True)
        self.box1.pack_start(self.sendto_combo, True, True, 0)

        

        #Text Box        
        self.textedit = gtk.TextView()
 
        self.textbuffer1 = self.textedit.get_buffer()
        self.scrolled_window = gtk.ScrolledWindow()
        self.scrolled_window.set_policy(gtk.POLICY_AUTOMATIC, gtk.POLICY_AUTOMATIC)
        self.textbuffer1.connect("changed",self.callback_func)
        
        self.textbuffer1.set_text(content)         #For Message Forward

        self.box1.pack_start(self.scrolled_window,True,True,5)
        self.scrolled_window.add(self.textedit)
        

        # Horizontal Box in the vertical box
        self.box3 = gtk.HBox(True, 5)
        self.box1.pack_start(self.box3, True, True, 0)


        # Save button to save the message in a file.
        self.button3 = gtk.Button("Save")
        self.button3.connect("clicked", self.save_msg, "button 3")
        self.box3.pack_start(self.button3, True, True, 0)

        # Send button to send message i.e. write to file.
        self.button2 = gtk.Button("Send")
        self.button2.connect("clicked", self.sending_msg, "button 2")
        self.box3.pack_start(self.button2, True, True, 0)

        # Discard button to get read and discard message.
        self.button4 = gtk.Button("Discard")
        self.button4.connect("clicked", self.delete_event, "button 4")
        self.box3.pack_start(self.button4, True, True, 0)

        # Progress bar to show sending msg
        self.pbar= gtk.ProgressBar()
        self.pbar.set_text("Sending Message...")
        self.box1.pack_start(self.pbar, True, True, 0)
      
        self.now = datetime.datetime.now()

        # Show Widgets
        self.label1.show()
        self.label2.show()
        self.sendto_combo.show()
        self.textedit.show()
        self.scrolled_window.show()
        self.button2.show()
        self.button3.show()
        self.button4.show()
        #self.pbar.show()
        self.box1.show()
        self.box2.show()
        self.box3.show()
        self.window.show()
        
                               

def main(from_no,content):
    cmpo = msgcompose(from_no,content)
    gtk.main()


if __name__ == "__main__":
    main('','')

import pygtk
pygtk.require('2.0')
import gtk
import ast

#import msg_compose

class msgread:     

    # Saving the Message Locally
    def save_msg(self, widget, data):
        var = raw_input("Enter file name: ")
        fil = open('Saved_Messages/' + var,'w')
        fil.writelines('From number: ' + ' ')
        startiter, enditer = self.buffer.get_bounds()
        msg = self.buffer.get_text(startiter, enditer, include_hidden_chars=True)
        fil.writelines('\nContents: ' + msg)
        fil.writelines('\nDate: ')
        fil.writelines('\nTime: ')
        fil.close()
        
        raw_input('Message Saved!')
        gtk.main_quit()
        return False
        

    """# Forwarding the received message
    def forwarding_msg(self, widget, data):
        msg_compose.main()
        
        
    # Replying to the message
    def replyto_msg(self, widget, data):
    """

    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        gtk.main_quit()
        return False


    # The GUI Description of the Window
    def __init__(self):
    
        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Read Message")
        self.window.connect("delete_event", self.delete_event)
        self.window.set_border_width(10)
        self.window.set_size_request(300,200)
        self.window.set_position(gtk.WIN_POS_CENTER)
        
        # Vertical box to pack widgets
        self.box1 = gtk.VBox(False, 5)
        self.window.add(self.box1)


        """# Horizontal Box in the vertical box
        self.box2 = gtk.HBox(True, 0)
        self.box1.pack_start(self.box2, True, True, 0)
        """
      
        # Label to dislpay sender
        self.from_label = gtk.Label()
        
        fob=open('dwrite', 'r')
        msg=fob.readlines()
        #print message
        fob.close()
        foo = ast.literal_eval(msg[0])

        self.from_label.set_label("From: " + `foo['data']['from_number']`)
        self.box1.pack_start(self.from_label, True, True, 0)


        #Text Box
        self.buffer= gtk.TextBuffer()
        self.textedit = gtk.TextView()
        self.textedit.set_wrap_mode(True)
        self.textedit.set_editable(False)
        self.textedit.set_buffer(self.buffer)

        """fob=open('dwrite', 'r')
        msg=fob.readlines()
        #print message
        fob.close()
        foo = ast.literal_eval(msg[0])
        #print foo
        #print foo['content']
        #mmm = foo['content']"""
        self.buffer.set_text(foo['data']['message'])

        self.scrolled_window = gtk.ScrolledWindow()
        self.scrolled_window.set_policy(gtk.POLICY_AUTOMATIC, gtk.POLICY_AUTOMATIC)
        
        #self.textedit.vbox.pack_start(scrolled_window)
        self.box1.pack_start(self.scrolled_window,True,True,5)
        self.scrolled_window.add(self.textedit)


        # Horizontal Box in the vertical box
        self.box3 = gtk.HBox(True, 5)
        self.box1.pack_start(self.box3, True, True, 0)


        # Reply button to compose a new msg to reply.
        self.button4 = gtk.Button("Reply")
        #self.button4.connect("clicked", self.replyto_msg, "button 4")
        self.box3.pack_start(self.button4, True, True, 0)

        # Forward button to forward the message.
        self.button2 = gtk.Button("Forward")
        #self.button2.connect("clicked", self.forwarding_msg, "button 2")
        self.box3.pack_start(self.button2, True, True, 0)

        # Discard button to discard the message after reading.
        self.button3 = gtk.Button("Discard")
        self.button3.connect("clicked", self.delete_event, "button 3")
        self.box3.pack_start(self.button3, True, True, 0)

        # Save button to save the message in a file.
        self.button1 = gtk.Button("Save")
        self.button1.connect("clicked", self.save_msg, "button 1")
        self.box3.pack_start(self.button1, True, True, 0)


        # Show Widgets
        self.from_label.show()
        self.textedit.show()
        self.scrolled_window.show()
        self.button1.show()
        self.button2.show()
        self.button3.show()
        self.button4.show()
        self.box1.show()
        #self.box2.show()
        self.box3.show()
        self.window.show()
        
                
        

def main():
    print 'done 2...'
    msread = msgread()
    gtk.main()


if __name__ == "__main__":
    print 'done 3...'
    #msread = msgread()
    main()

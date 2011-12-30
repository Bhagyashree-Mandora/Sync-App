import pygtk
pygtk.require('2.0')
import gtk
import ast


class msgread:     
    
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


        # Horizontal Box in the vertical box
        self.box2 = gtk.HBox(True, 0)
        self.box1.pack_start(self.box2, True, True, 0)


        #Text Box
        self.buffer= gtk.TextBuffer()
        self.textedit = gtk.TextView()
        self.textedit.set_wrap_mode(True)
        self.textedit.set_editable(False)
        self.textedit.set_buffer(self.buffer)

        fob=open('dwrite', 'r')
        message=fob.readlines()
        #print message
        fob.close()
        foo = ast.literal_eval(message[0])
        #print foo
        #print foo['content']
        #mmm = foo['content']
        self.buffer.set_text(foo['content'])

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
        #self.button4.connect("clicked", self., "button 4")
        self.box3.pack_start(self.button4, True, True, 0)

        # Forward button to forward the message.
        self.button2 = gtk.Button("Forward")
        #self.button2.connect("clicked", self., "button 2")
        self.box3.pack_start(self.button2, True, True, 0)

        # Discard button to discard the message after reading.
        self.button3 = gtk.Button("Discard")
        #self.button3.connect("clicked", self., "button 3")
        self.box3.pack_start(self.button3, True, True, 0)

        # Show Widgets
        self.textedit.show()
        self.scrolled_window.show()
        self.button2.show()
        self.button3.show()
        self.button4.show()
        self.box1.show()
        self.box2.show()
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

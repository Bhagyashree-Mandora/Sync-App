import pygtk
pygtk.require('2.0')
import gtk
import json_crafting


class msgcompose:

    # On clicking the send button, takes text from buffer
    def sending_msg(self, widget, data):
        self.pbar.show()
        startiter, enditer = self.textbuffer1.get_bounds()
        msg = self.textbuffer1.get_text(startiter, enditer, include_hidden_chars=True)
        print msg

        json_data = [{'content':msg, 'date':'27', 'time':'4:30', 'sender':'ME'}]
        
        craft = json_crafting.Crafting()
        craft.input_json_data(json_data)
        print json_data
        #self.pbar.hide()

   
    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        #self.textbuffer1.delete(0)
        gtk.main_quit()
        return False


    # The GUI Description of the Window
    def __init__(self):
    
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


        #Text Box
        self.textedit = gtk.TextView()
        self.textbuffer1 = self.textedit.get_buffer()
        self.scrolled_window = gtk.ScrolledWindow()
        self.scrolled_window.set_policy(gtk.POLICY_AUTOMATIC, gtk.POLICY_AUTOMATIC)
        self.box1.pack_start(self.scrolled_window,True,True,5)
        self.scrolled_window.add(self.textedit)


        # Horizontal Box in the vertical box
        self.box3 = gtk.HBox(True, 5)
        self.box1.pack_start(self.box3, True, True, 0)


        # Account Info button to get info of users account.
        self.button4 = gtk.Button("Discard")
        self.button4.connect("clicked", self.delete_event, "button 4")
        self.box3.pack_start(self.button4, True, True, 0)

        # Send button to write to file.
        self.button2 = gtk.Button("Send")
        self.button2.connect("clicked", self.sending_msg, "button 2")
        self.box3.pack_start(self.button2, True, True, 0)

        # Get button to read a file.
        self.button3 = gtk.Button("Save")
        #self.button3.connect("clicked", self., "button 3")
        self.box3.pack_start(self.button3, True, True, 0)

        # Progress bar to show sending msg
        self.pbar= gtk.ProgressBar()
        self.pbar.set_text("Sending Message...")
        self.box1.pack_start(self.pbar, True, True, 0)
        

        # Show Widgets
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
        
                               

def main():
    gtk.main()


if __name__ == "__main__":
    cmpo = msgcompose()
    main()

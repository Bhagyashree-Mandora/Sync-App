import pygtk
pygtk.require('2.0')
import gtk

import msg_compose
#import unread_index


class msgapp:

    # Calls the compose message window function
    def compse_msg(self, widget, data):
        print 'plz open...'
        msg_compose.main()
        print 'ab khul bhi ja yaar..'
        gtk.main_quit()
        
    # Shows index of unread messages in a window
    def unread(self, widget, data):
       
        #unread_index.main()
        gtk.main_quit()

    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        gtk.main_quit()
        return False


    # The GUI Description of the Window
    def __init__(self):
    
        # Icon trial
        self.info =  gtk.StatusIcon()
        self.info.set_from_icon_name("Our App")
        #self.info.set_from_gicon("msg.jpg")

        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Message App")
        self.window.connect("delete_event", self.delete_event)
        self.window.set_border_width(10)
        self.window.set_size_request(200,100)
        self.window.set_position(gtk.WIN_POS_CENTER)
        
        # Vertical box to pack widgets
        self.box1 = gtk.VBox(False, 5)
        self.window.add(self.box1)

        # Compose button to create a new message.
        self.button1 = gtk.Button("Create New")
        self.button1.connect("clicked", self.compse_msg, "button 1")
        self.box1.pack_start(self.button1, True, True, 0)

        # Unread button to send message i.e. write to file.
        self.button2 = gtk.Button("You have an unread message")
        self.button2.connect("clicked", self.unread, "button 2")
        self.box1.pack_start(self.button2, True, True, 0)
        

        # Show Widgets
        self.button1.show()
        self.button2.show()
        self.box1.show()
        self.window.show()
        
                               

def main():
    gtk.main()


if __name__ == "__main__":
    app = msgapp()
    main()

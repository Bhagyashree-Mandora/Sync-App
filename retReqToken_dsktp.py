#!/usr/bin/env python

import pygtk
pygtk.require('2.0')
import gtk
from nose.tools import *
from dropbox import auth
from helpers import login_and_authorize
import httplib2
from httplib2 import socks



CALLBACK_URL = 'http://printer.example.com/request_token_ready'
RESOURCE_URL = 'http://www.dropbox.com/0/oauth/echo'


class WriteOperation:

    # This function writes the data entered in the text box to
    # the file in Dropbox folder used for writing
    def writetofile(self, widget, data):
        print "Send button is clicked..."
#        text = self.entry.get_text()
        #httplib2.debuglevel=4
	#h = httplib2.Http(proxy_info = httplib2.ProxyInfo(socks.PROXY_TYPE_HTTP, '192.168.14.254', 3128))
        # This location of file is to be taken from the user's laptop
        # A script which finds the location of the Dropbox folder 
        # needs to be run
	config = auth.Authenticator.load_config("testing.ini")
	dba = auth.Authenticator(config)
	token = dba.obtain_request_token()
	text.set_text(token)
	#assert dba.oauth_request
	#return token
        
    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        gtk.mainquit()
        return gtk.FALSE


    # The GUI Description of the Window
    def __init__(self):
    
        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Our App")
        self.window.connect("delete_event", self.delete_event)
        self.window.set_border_width(10)

        # Horizontal box to pack widgets
        self.box1 = gtk.HBox(gtk.FALSE, 0)
        self.window.add(self.box1)

        # Text Box for text input
        self.entry = gtk.Entry()
        self.box1.pack_start(self.entry, gtk.TRUE, gtk.TRUE, 0)
        self.entry.show()

        # Send button. Works on Click event
        self.button2 = gtk.Button("Get")
        self.button2.connect("clicked", self.writetofile, "button 2")
        self.box1.pack_start(self.button2, gtk.TRUE, gtk.TRUE, 0)

        # Show Widgets
        self.button2.show()
        self.box1.show()
        self.window.show()


def main():
    gtk.mainloop()


if __name__ == "__main__":
    wriop = WriteOperation()
    main()

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


class oAuthProcess:

    def authorisation(self, widget, data):
       
        print "Send button is clicked..."

        # creates a dict which stores key and secret
	config = auth.Authenticator.load_config("testing.ini")
        
        for key in ['server', 'port', 'consumer_key', 'consumer_secret', 'verifier']:
            print config[key]
 
	dba = auth.Authenticator(config)
        
        # gets temporary token
	req_token = dba.obtain_request_token()	
        print req_token
        
        # directs user to get user permission
        authorize_url = dba.build_authorize_url(req_token, callback="http://localhost/")       
        assert "localhost" in authorize_url, "Should have the return address in it."

          # authorize_url = dba.build_authorize_url(req_token)
          # login_and_authorize(authorize_url, config)
       
        print "Goto this Url: ", authorize_url

        # Request token obtained after permission is used to get access token
        req_token.key = raw_input("Enter the token you got:")
        access_token = dba.obtain_access_token(req_token, config['verifier'])
 
        assert access_token
        assert dba.oauth_request
        assert access_token.key != req_token.key
        
        print access_token
        

    def test_build_access_headers():
        token, dba = test_obtain_access_token()
        assert token
        headers, params = dba.build_access_headers("GET", token, RESOURCE_URL, {'image': 'test.png'}, CALLBACK_URL)
        assert headers
        assert params

    
        
    # Causing the close button to close the window
    def delete_event(self, widget, event, data=None):
        gtk.main_quit
        return False


    # The GUI Description of the Window
    def __init__(self):
    
        # Create a new window
        self.window = gtk.Window(gtk.WINDOW_TOPLEVEL)
        self.window.set_title("Our App")
        self.window.connect("delete_event", self.delete_event)
        self.window.set_border_width(10)

        # Horizontal box to pack widgets
        self.box1 = gtk.HBox(False, 0)
        self.window.add(self.box1)

        # Text Box for text input
        self.entry = gtk.Entry()
        self.box1.pack_start(self.entry, True, True, 0)
        self.entry.show()

        # Send button. Works on Click event
        self.button2 = gtk.Button("Get")
        self.button2.connect("clicked", self.authorisation, "button 2")
        self.box1.pack_start(self.button2, True, True, 0)

        # Show Widgets
        self.button2.show()
        self.box1.show()
        self.window.show()


def main():
    gtk.mainloop()


if __name__ == "__main__":
    authobj = oAuthProcess()
    main()

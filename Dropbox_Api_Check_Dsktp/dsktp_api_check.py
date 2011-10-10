#!/usr/bin/env python

import pygtk
pygtk.require('2.0')
import gtk
from nose.tools import *
from dropbox import auth, client, rest
from helpers import login_and_authorize
import httplib2
from httplib2 import socks
from oauth import oauth



class ApiCheck:

   
    # This function writes the data entered in the text box to 
    # the file used for writing data
    def put_file(self,widget,data):
        
        
        #An object of the Authenticator class from oAuth authentication of our App        
        config = auth.Authenticator.load_config("testing.ini")
        
        dba = auth.Authenticator(config)     
        access_token=oauth.OAuthToken('r3ie1kzqr0ipxil', 'dbcqrsddgihp3w6')
 
        print access_token

        #An object of Client is created
        db_client = client.DropboxClient(config['server'], config['content_server'], config['port'], dba, access_token)
        root = config['root']

        
        # Writes the data entered in the text box to the file
        print "Send button is clicked..."
        text = self.entry.get_text()
        
        # This location of file is to be taken from the user's laptop
        # A script which finds the location of the Dropbox folder 
        # needs to be run 
        myfile = open('ourfile.txt','w')
	    
        myfile.truncate()
        myfile.write(text)       
        myfile.close()        
  
        
        f = open("ourfile.txt")
        resp = db_client.put_file(root, "/", f)
        #assert resp
        
        
        assert_equal(resp.status, 200)
                

    # gets the account information of the user
    def account_info(self,widget,data):

        
        # An object of the Authenticator class from oAuth authentication of our App        
        config = auth.Authenticator.load_config("testing.ini")
        
        dba = auth.Authenticator(config)     
        access_token=oauth.OAuthToken('r3ie1kzqr0ipxil', 'dbcqrsddgihp3w6')
 
        print access_token

        # An object of Client is created
        db_client = client.DropboxClient(config['server'], config['content_server'], config['port'], dba, access_token)
        root = config['root']


        # Gets the account info of user
        db_client.file_delete(root, "/tohere")
        resp = db_client.account_info()
        assert resp
        print resp.data
        # assert_equal(resp.status, 200)
        # assert_all_in(resp.data.keys(), [u'country', u'display_name', u'uid', u'quota_info'])
    
        
    # reads data from the file
    def get_file(self,widget,data):

        # An object of the Authenticator class from oAuth authentication of our App        
        config = auth.Authenticator.load_config("testing.ini")
        
        dba = auth.Authenticator(config)     
        access_token=oauth.OAuthToken('r3ie1kzqr0ipxil', 'dbcqrsddgihp3w6')
 
        print access_token

        # An object of Client is created
        db_client = client.DropboxClient(config['server'], config['content_server'], config['port'], dba, access_token)
        root = config['root']


        # Prints the retrieved file
        resp = db_client.get_file(root, "/ourfile.txt")
        assert_equal(resp.status, 200)
        print resp.read()
        #assert len(resp.read()) > 100

        return db_client



        
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
        self.window.set_position(gtk.WIN_POS_CENTER)
        
        # Horizontal box to pack widgets
        self.box1 = gtk.HBox(False, 0)
        self.window.add(self.box1)

        # Text Box for text input
        self.entry = gtk.Entry()
        self.box1.pack_start(self.entry, True, True, 0)
        self.entry.show()

        # Account Info button to get info of users account.
        self.button4 = gtk.Button("Acc Info")
        self.button4.connect("clicked", self.account_info, "button 4")
        self.box1.pack_start(self.button4, True, True, 0)

        # Send button to write to file.
        self.button2 = gtk.Button("Send")
        self.button2.connect("clicked", self.put_file, "button 2")
        self.box1.pack_start(self.button2, True, True, 0)

        # Get button to read a file.
        self.button3 = gtk.Button("Get")
        self.button3.connect("clicked", self.get_file, "button 3")
        self.box1.pack_start(self.button3, True, True, 0)

        # Show Widgets
        self.button2.show()
        self.button3.show()
        self.button4.show()
        self.box1.show()
        self.window.show()
        
                
               

def main():
    gtk.mainloop()


if __name__ == "__main__":
    check = ApiCheck()
    main()


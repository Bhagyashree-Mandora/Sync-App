import pickle

class Handler:

    def __init__(self,json_data):

        # unpickle the object corresponding to the command in json object.
        # The file name of the pickle is the same as command.
        # The pickled object holds the program to be executed on receipt of the json object
        
        #for item in json_data
            
        print json_data[0]['command']
        cmd = json_data[0]['command'] + '.pkl'
        print cmd
        pkl_file = open(cmd, 'rb')
        obj = pickle.load(pkl_file)
        pkl_file.close()

        # Reinitialise (reassign) the object with the new values in the json.
        

        obj.__init__(json_data)

        # if
        obj.OnReceive()
        #obj.OnReceive(obj.data_str)
        #obj.write(obj.data_str)  #to be tested
        
        #if json_data['type'] == 'response'
            

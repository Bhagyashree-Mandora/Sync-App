import sys
import send
import ast

print sys.argv[1]
print sys.argv[2]

foo1 = sys.argv[2]
foo = ast.literal_eval(foo1)

sobj=send.Send(sys.argv[1],foo)

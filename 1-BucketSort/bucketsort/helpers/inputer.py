###
### Example usage:
###   $ python inputer.py -n 1000
###
### This produces N*94 uniformly distributed keys, sending to stdout.
###

from __future__ import print_function
import argparse
import random
import sys

parser = argparse.ArgumentParser(description='Random input generator for bucketsort problem')
parser.add_argument('-n', '--numkeys',
                    required=True,
                    help='Number of keys-iterations')
parser.add_argument('-s', '--seed',
                    default="0",
                    help='Seed for random generator')

def main():
    args = parser.parse_args()

    N = int(args.numkeys)
    random.seed(int(args.seed))

    print(N*94)
    for i in range(N):
        chars = range(0x21, 0x7E + 1)
        random.shuffle(chars)
        for j in range(len(chars)):
            c1 = chr(chars[j])
            c2 = chr(random.randint(0x21, 0x7E))
            c3 = chr(random.randint(0x21, 0x7E))
            c4 = chr(random.randint(0x21, 0x7E))
            c5 = chr(random.randint(0x21, 0x7E))
            c6 = chr(random.randint(0x21, 0x7E))
            c7 = chr(random.randint(0x21, 0x7E))
            s = c1 + c2 + c3 + c4 + c5 + c6 + c7
            print(s)

if __name__ == "__main__":
    main()

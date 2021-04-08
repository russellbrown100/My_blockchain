# My_blockchain
This project proposes and demonstrates various routines that could be used in a blockchain.  The intention is to have a node that act's like a server that maintains its own immutable database.  The node is part of a peer to peer network of other nodes.  The node synchronizes every 2 minutes.  During synchronizing the database is verified against the peer nodes.  Once verified the database is compressed and is transferred to a permanent database.  When a node receives data to be added from a client the data is broadcasted and achknowledged by peers prior to being accepted by the node.  The client encrypts the data before informing the node to add the transaction.

Documentation and code comments are not yet published.

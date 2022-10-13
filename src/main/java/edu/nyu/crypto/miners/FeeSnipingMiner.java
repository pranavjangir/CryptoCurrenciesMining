package edu.nyu.crypto.miners;


import edu.nyu.crypto.blockchain.Block;
import edu.nyu.crypto.blockchain.NetworkStatistics;

public class FeeSnipingMiner extends CompliantMiner implements Miner {


    protected Block local_head;
    
    public FeeSnipingMiner(String id, int hashRate, int connectivity) {
        super(id, hashRate, connectivity);

    }

    public Block currentlyMiningAt() {
        return local_head;
    }

    public Block currentHead() {
        return local_head;
    }

    public void blockMined(Block block, boolean isMinerMe) {
        if(isMinerMe) {
            if (block.getHeight() > local_head.getHeight()) {
                this.local_head = block;
            }
        }
        else {
            if (block.getHeight() > local_head.getHeight() + 1) {
                this.local_head = block;
                return;
            }
            if (block.getHeight() == local_head.getHeight() + 1) {
                if (block.getBlockValue() > 10) {
                    // Do nothing, let us try to fork!
                } else {
                    this.local_head = block;
                }
           }
        }
    }


    public void initialize(Block genesis, NetworkStatistics networkStatistics) {
        this.currentHead = genesis;
        this.local_head = genesis;
    }



	// TODO Override methods to implement Fee Sniping
}

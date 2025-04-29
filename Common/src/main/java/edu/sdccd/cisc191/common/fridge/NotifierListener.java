package edu.sdccd.cisc191.common.fridge;

public interface NotifierListener {
    void onItemsExpired(String[][] expiredItems);
}

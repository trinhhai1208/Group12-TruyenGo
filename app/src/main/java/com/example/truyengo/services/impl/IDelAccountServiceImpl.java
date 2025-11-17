package com.example.truyengo.services.impl;

import static com.example.truyengo.dao.ConnectDB.collection;
import static com.example.truyengo.dao.ConnectDB.collectionFavourite;
import static com.example.truyengo.dao.ConnectDB.collectionHistory;
import static com.example.truyengo.dao.ConnectDB.collectionInfo;
import static com.example.truyengo.dao.ConnectDB.collectionIp;

import com.example.truyengo.services.IDelAccountService;

import org.bson.Document;
import org.bson.types.ObjectId;

public class IDelAccountServiceImpl implements IDelAccountService {
    public void delAccount(ObjectId idUser) {
        try {
            collection.deleteMany(new Document("_id", idUser));
            collectionHistory.deleteMany(new Document("userId", idUser));
            collectionInfo.deleteMany(new Document("_id", idUser));
            collectionIp.deleteMany(new Document("_id", idUser));
            collectionFavourite.deleteMany(new Document("_id", idUser));
        } catch (Exception e) {
            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
        }
    }
}

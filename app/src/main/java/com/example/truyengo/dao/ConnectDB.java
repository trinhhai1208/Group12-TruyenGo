package com.example.truyengo.dao;

import com.example.truyengo.models.book.Book;
import com.example.truyengo.models.user.UserAccount;
import com.example.truyengo.models.user.UserFavouriteBook;
import com.example.truyengo.models.user.UserHistoryBooks;
import com.example.truyengo.models.user.UserIP;
import com.example.truyengo.models.user.UserInfo;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class ConnectDB {
    public static final String URI = "mongodb+srv://Vuong:Dinhvuong2005@cluster0.uexir.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    public static final ServerApi SERVER_API = ServerApi.builder().version(ServerApiVersion.V1).build();

    public static final CodecProvider POJO_CODEC_PROVIDER = PojoCodecProvider.builder().automatic(true).build();

    public static final CodecRegistry POJO_CODEC_REGISTRY = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(POJO_CODEC_PROVIDER));

    public static final MongoClientSettings SETTINGS = MongoClientSettings.builder().applyConnectionString(new ConnectionString(URI)).codecRegistry(POJO_CODEC_REGISTRY)
            .serverApi(SERVER_API).build();

    public static final MongoClient MONGO_CLIENT = MongoClients.create(SETTINGS);

    public static final MongoDatabase db = MONGO_CLIENT.getDatabase("HITDB").withCodecRegistry(POJO_CODEC_REGISTRY);

    public static MongoCollection<UserAccount> collection = db.getCollection("UserAccount", UserAccount.class);
    public static MongoCollection<UserInfo> collectionInfo = db.getCollection("UserInfo", UserInfo.class);
    public static MongoCollection<UserIP> collectionIp = db.getCollection("UserIP", UserIP.class);
    public static MongoCollection<UserHistoryBooks> collectionHistory = db.getCollection("UserHistoryBooks", UserHistoryBooks.class);
    public static MongoCollection<Book> collectionBook = db.getCollection("Book", Book.class);
    public static MongoCollection<UserFavouriteBook> collectionFavourite = db.getCollection("UserFavouriteBook", UserFavouriteBook.class);
}

//package com.example.truyengo.services.impl;
//
//import static com.example.truyengo.commons.CurrentUser.userAccount;
//import static com.example.truyengo.dao.ConnectDB.collectionBook;
//import static com.example.truyengo.dao.ConnectDB.collectionFavourite;
//import static com.example.truyengo.dao.ConnectDB.collectionHistory;
//
//import android.graphics.Color;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
//import com.example.truyengo.models.book.Book;
//import com.example.truyengo.models.user.UserFavouriteBook;
//import com.example.truyengo.models.user.UserHistoryBooks;
//import com.example.truyengo.services.IBookService;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.Sorts;
//import com.mongodb.client.model.UpdateOptions;
//import com.mongodb.client.model.Updates;
//
//
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//
//public class IBookServiceImpl implements IBookService {
//    private UserFavouriteBook foundFav;
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void storageBookToUser(ObjectId bookId) {
//        try {
//            if (!checkIfExitBookInUser(bookId)) {
//                UserHistoryBooks userHistoryBooks = new UserHistoryBooks(userAccount.getId(), bookId, 1, LocalDateTime.now());
//                collectionHistory.insertOne(userHistoryBooks);
//            }
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//        }
//    }
//
//    public int getLastReadIndexChapter(ObjectId bookId) {
//        try {
//            Bson filter = Filters.and(Filters.eq("userId", userAccount.getId()), Filters.eq("bookId", bookId));
//            UserHistoryBooks result = collectionHistory.find(filter).first();
//            return (result != null) ? result.getLastReadChapter() : 1;
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//            return 1;
//        }
//    }
//
//    public void saveLastReadChapter(ObjectId bookId, double chapter) {
//        try {
//            Bson filter = Filters.and(Filters.eq("userId", userAccount.getId()), Filters.eq("bookId", bookId));
//            collectionHistory.updateOne(filter, Updates.set("lastReadChapter", chapter));
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//        }
//    }
//
//    public boolean checkIfExitBookInUser(ObjectId bookId) {
//        try {
//            Bson filter = Filters.and(Filters.eq("userId", userAccount.getId()), Filters.eq("bookId", bookId));
//            UserHistoryBooks userHistoryBooks = collectionHistory.find(filter).first();
//
//            if (userHistoryBooks != null) {
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//            return false;
//        }
//    }
//
//    public void updateDateToRead(ObjectId bookId) {
//        try {
//            Bson filter = Filters.and(Filters.eq("userId", userAccount.getId()), Filters.eq("bookId", bookId));
//            UserHistoryBooks userHistoryBooks = collectionHistory.find(filter).first();
//            if (userHistoryBooks != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    collectionHistory.updateOne(filter, Updates.set("lastReadDate", LocalDateTime.now()));
//                }
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    collectionHistory.insertOne(new UserHistoryBooks(userAccount.getId(), bookId, 1, LocalDateTime.now()));
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public void insertBookToDB(Book book) {
//        try {
//            collectionBook.updateOne(Filters.eq("_id", book.getId()), new Document("$setOnInsert", book), new UpdateOptions().upsert(true));
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//        }
//    }
//
//    public Book getBookById(ObjectId bookId) {
//        try {
//            return collectionBook.find(Filters.eq("_id", bookId)).first();
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//            return null;
//        }
//    }
//
//    public ArrayList<Book> getBooksByPage(int pageNumber, int pageSize) {
//        ArrayList<Book> books = new ArrayList<>();
//        try {
//            // Sử dụng skip và limit để phân trang
//            FindIterable<UserHistoryBooks> results = collectionHistory
//                    .find(new Document("userId", userAccount.getId()))
//                    .sort(Sorts.descending("lastReadDate"))
//                    .skip((pageNumber - 1) * pageSize)
//                    .limit(pageSize);
//
//            for (UserHistoryBooks history : results) {
//                Book book = getBookById(history.getBookId());
//                if (book != null) {
//                    books.add(book);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//        }
//        return books;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void toggleFavorite(ObjectId bookId, Color level) {
//        try {
//            Bson filter = Filters.and(Filters.eq("userId", userAccount.getId()), Filters.eq("bookId", bookId));
//            if (foundFav == null) {
//                foundFav = new UserFavouriteBook(userAccount.getId(), bookId, true, LocalDateTime.now());
//                collectionFavourite.insertOne(foundFav);
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    collectionFavourite.updateOne(
//                            filter,
//                            Updates.combine(
//                                    Updates.set("favourite", (Color.RED)),
//                                    Updates.set("lastAddFavDate", LocalDateTime.now())
//                            ),
//                            new UpdateOptions().upsert(true)
//                    );
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public ArrayList<Book> getFavoritesByPage(int pageNumber, int pageSize) {
//        ArrayList<Book> books = new ArrayList<>();
//        try {
//            Bson filter = Filters.and(
//                    Filters.eq("userId", userAccount.getId()),
//                    Filters.eq("favourite", true)
//            );
//
//
//            FindIterable<UserFavouriteBook> results = collectionFavourite
//                    .find(filter)
//                    .sort(Sorts.descending("lastAddFavDate"))
//                    .skip((pageNumber - 1) * pageSize)
//                    .limit(pageSize);
//
//            for (UserFavouriteBook history : results) {
//                Book book = getBookById(history.getBookId());
//                if (book != null) {
//                    books.add(book);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
//        }
//        return books;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public Color checkFavorite(ObjectId bookId) {
//        try {
//            foundFav = collectionFavourite.find(Filters.and(Filters.eq("userId", userAccount.getId()), Filters.eq("bookId", bookId))).first();
//            if (foundFav == null) {
//                return Color.valueOf(Color.GRAY);
//            } else {
//                return Color.valueOf(((foundFav.getFavourite()) ? Color.RED : Color.GRAY));
//            }
//        } catch (NullPointerException e) {
//            return Color.valueOf(Color.GRAY);
//        }
//    }
//
//}

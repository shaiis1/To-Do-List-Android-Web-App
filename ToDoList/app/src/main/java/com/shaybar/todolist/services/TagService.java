package com.shaybar.todolist.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.webkit.JavascriptInterface;

import com.shaybar.todolist.entities.Tag;
import com.shaybar.todolist.utils.AppUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TagService {
    private Context context;

    public TagService(Context context) {
        this.context = context;
    }

    /**
     * return sorted tags mapped by their id,
     * id => tag entity
     */
    @JavascriptInterface
    public String getAllTags() {
        SQLiteDatabase db = DatabaseHelper.getReadableDatabase(getContext());
        Cursor cursor = db.query(Tag.TABLE_NAME, null, null, null, null, null, null);

        List<Tag> tags = DatabaseHelper.getOrm().listFromCursor(cursor, Tag.class);
        Collections.sort(tags, new Comparator<Tag>() {
            public int compare(Tag a, Tag b) {
                return a.getName().compareTo(b.getName());
            }
        });

        Map<Long, Tag> tagMap = new HashMap<>();
        for (Tag tag : tags) {
            tagMap.put(tag.getId(), tag);
        }

        cursor.close();
        db.close();
        return AppUtils.toJson(tagMap);
    }

    /**
     * getTagById
     * <p>
     * return the requested tag by tag id, or null if not exist.
     */
    @JavascriptInterface
    public Tag getTagById(String tagId) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        Cursor cursor = db.query(Tag.TABLE_NAME, null, "_id=" + tagId, null, null, null, null);
        List<Tag> tagList = DatabaseHelper.getOrm().listFromCursor(cursor, Tag.class);
        cursor.close();
        db.close();
        return (tagList.size() == 0) ? null : tagList.get(0);
    }

    private long saveTag(Tag tag, String id) {
        SQLiteDatabase db = DatabaseHelper.getWritableDatabase(getContext());
        ContentValues insertValues = new ContentValues();
        insertValues.put(Tag.NAME, tag.getName());
        insertValues.put(Tag.COLOR, tag.getColor());
        long responseCode;

        if (id != null) {
            String[] vec = {id};
            responseCode = db.update(Tag.TABLE_NAME, insertValues, BaseColumns._ID + "=?", vec);
        } else {
            responseCode = db.insert(Tag.TABLE_NAME, null, insertValues);
        }

        db.close();
        return responseCode;
    }

    /**
     * saveTag
     * <p>
     * save or update tag after parse tag form.
     * return SUCCESS or FAILED message.
     *
     * @return
     */
    @JavascriptInterface
    public String saveTag(String json, String id) {
        long responseCode = -1L;

        try {
            Tag tag = AppUtils.fromJson(json, Tag.class);
            responseCode = saveTag(tag, id);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        } finally {
            if (responseCode > 0) {
                return AppUtils.toJson(new Message(MessageType.INFO, MessagesFactory.ACTION_COMPLETED, "Save Tag"));
            } else {
                return AppUtils.toJson(new Message(MessageType.ERROR, MessagesFactory.UNABLE_TO_SAVE_TAG));
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}



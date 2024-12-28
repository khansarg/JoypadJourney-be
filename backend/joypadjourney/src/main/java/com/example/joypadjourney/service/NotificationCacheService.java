package com.example.joypadjourney.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class NotificationCacheService {

    // Map untuk menyimpan notifikasi sementara berdasarkan username
    private final ConcurrentHashMap<String, List<String>> notifications = new ConcurrentHashMap<>();

    /**
     * Tambahkan notifikasi baru untuk pengguna tertentu.
     *
     * @param username Nama pengguna yang akan menerima notifikasi.
     * @param message  Pesan notifikasi.
     */
    public void addNotification(String username, String message) {
        notifications.computeIfAbsent(username, k -> new ArrayList<>()).add(message);
    }

    /**
     * Dapatkan semua notifikasi untuk pengguna tertentu.
     *
     * @param username Nama pengguna.
     * @return Daftar notifikasi untuk pengguna, atau daftar kosong jika tidak ada notifikasi.
     */
    public List<String> getNotifications(String username) {
        return notifications.getOrDefault(username, new ArrayList<>());
    }

    /**
     * Hapus semua notifikasi untuk pengguna tertentu.
     *
     * @param username Nama pengguna.
     */
    public void clearNotifications(String username) {
        notifications.remove(username);
    }

    /**
     * Hapus notifikasi tertentu untuk pengguna.
     *
     * @param username Nama pengguna.
     * @param message  Pesan notifikasi yang ingin dihapus.
     */
    public void removeNotification(String username, String message) {
        notifications.computeIfPresent(username, (key, userNotifications) -> {
            userNotifications.remove(message);
            return userNotifications.isEmpty() ? null : userNotifications;
        });
    }
}

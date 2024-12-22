package com.jcosta.tinybank.adapters.out.storage.inmemory;

import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedList<T> {
    private final ReentrantLock lock = new ReentrantLock();
    private Node head;
    private int size = 0;

    public void add(int index, T value) {
        this.lock.lock();
        try {
            if (index < 0 || index > this.size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
            }

            Node newNode = new Node(value);

            if (index == 0) {
                newNode.next = this.head;
                this.head = newNode;
            } else {
                Node prev = getNodeAt(index - 1);
                newNode.next = prev.next;
                prev.next = newNode;
            }

            this.size++;
        } finally {
            this.lock.unlock();
        }
    }

    public T remove(int index) {
        this.lock.lock();
        try {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
            }

            Node removedNode;

            if (index == 0) {
                removedNode = head;
                head = head.next;
            } else {
                Node prev = getNodeAt(index - 1);
                removedNode = prev.next;
                prev.next = removedNode.next;
            }

            this.size--;
            return removedNode.value;
        } finally {
            this.lock.unlock();
        }
    }

    public T get(int index) {
        this.lock.lock();
        try {
            return getNodeAt(index).value;
        } finally {
            this.lock.unlock();
        }
    }

    public int size() {
        this.lock.lock();
        try {
            return this.size;
        } finally {
            this.lock.unlock();
        }
    }

    private Node getNodeAt(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }

        Node current = this.head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private class Node {
        T value;
        Node next;

        Node(T value) {
            this.value = value;
        }
    }
}

package org.gonnaup.tutorial.common.model;

/**
 * @author gonnaup
 * @version created at 2022/10/13 21:33
 */
public record Result<T>(String code, T data) {
}

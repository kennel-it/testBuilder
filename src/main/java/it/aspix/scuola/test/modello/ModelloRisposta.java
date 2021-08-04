package it.aspix.scuola.test.modello;

/****************************************************************************
 * 
 * Una possibile risposta in un modello ha un testo, può essere giusta
 * e può essere eliminata (ad esempio nei compiti per studenti con DSA)
 *
 ***************************************************************************/
public record ModelloRisposta(String testo, boolean giusta, boolean eliminabile) { }
/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.parsing;

import br.net.buzu.pplspec.model.Align;

/**
 * Classe com métodos utilitários.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class Text {

	private Text() {
	}

	/**
	 * Informa se a String é nula, vazia ou toda formada por espaços.
	 * 
	 * <pre>
	 * return text == null || text.trim().length() == 0;
	 * </pre>
	 * 
	 * @param text
	 *            O texto a ser avaliado.
	 * @return <code>true</code> se é vazia (Blank) ou <code>false</code> se não
	 *         for.
	 */
	public static boolean isBlank(final String text) {
		return text == null || text.trim().length() == 0;
	}

	/**
	 * Completa ou corta uma String de forma que ela fique com um tamanho fixo
	 * definido como parâmetro.
	 * 
	 * @param align
	 *            O tipo de alinhamento (direita ou esquerda).
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @param c
	 *            O caractere que preenche possíveis buracos.
	 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
	 *         tamanho.
	 */
	public static String fit(final Align align, final String original, final int finalSize, final char c) {
		if (original.length() == finalSize) {
			return original;
		}
		return (original.length() < finalSize) ? fill(align, original, finalSize, c) : cut(align, original, finalSize);
	}

	/**
	 * Completa ou corta uma String de forma que ela fique com um tamanho fixo
	 * definido como parâmetro com alinhamento á DIREITA.
	 * 
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @param c
	 *            O caractere que preenche possíveis buracos.
	 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
	 *         tamanho.
	 */
	public static String rightFit(final String original, final int finalSize, final char c) {
		if (original.length() == finalSize) {
			return original;
		}
		return (original.length() < finalSize) ? rightFill(original, finalSize, c) : rightCut(original, finalSize);
	}

	/**
	 * Completa ou corta uma String de forma que ela fique com um tamanho fixo
	 * definido como parâmetro com alinhamento á ESQUERDA.
	 * 
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @param c
	 *            O caractere que preenche possíveis buracos.
	 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
	 *         tamanho.
	 */
	public static String leftFit(final String original, final int finalSize, final char c) {
		if (original.length() == finalSize) {
			return original;
		}
		return (original.length() < finalSize) ? leftFill(original, finalSize, c) : leftCut(original, finalSize);
	}

	/**
	 * Corta uma String de forma que ela fique com um tamanho fixo definido como
	 * parâmetro.
	 * 
	 * @param align
	 *            O tipo de alinhamento (direita ou esquerda).
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
	 *         tamanho.
	 */
	public static String cut(final Align align, String original, int finalSize) {
		return (Align.RIGHT.equals(align)) ? leftCut(original, finalSize) : rightCut(original, finalSize);
	}

	/**
	 * Corta uma String à direita até que ela fique no tamanho final definido
	 * como parâmetro.
	 * 
	 * @param original
	 *            A String original que será cortada.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @return A String cortada e com o tamanho correto.
	 */
	public static String leftCut(String original, int finalSize) {
		return original.substring(original.length() - finalSize, original.length());
	}

	/**
	 * Corta uma String à direita até que ela fique no tamanho final definido
	 * como parâmetro.
	 * 
	 * @param original
	 *            A String original que será cortada.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @return A String cortada e com o tamanho correto.
	 */
	public static String rightCut(String original, int finalSize) {
		return original.substring(0, finalSize);
	}

	/**
	 * Completa uma String de forma que ela fique com um tamanho fixo definido
	 * como parâmetro.
	 * 
	 * @param align
	 *            O tipo de alinhamento (direita ou esquerda).
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @param c
	 *            O caractere que preenche possíveis buracos.
	 * @return A String no tamanho certo, preenchida de acordo com o tamanho.
	 */
	public static String fill(final Align align, String original, int fnalSize, char c) {
		return (Align.RIGHT.equals(align)) ? leftFill(original, fnalSize, c) : rightFill(original, fnalSize, c);
	}

	/**
	 * Completa uma String á direita de forma que ela fique com um tamanho fixo
	 * definido como parâmetro.
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @param c
	 *            O caractere que preenche possíveis buracos.
	 * @return A String no tamanho certo, preenchida de acordo com o tamanho.
	 */
	public static String rightFill(String original, int finalSize, char c) {
		StringBuilder sb = new StringBuilder(original);
		for (int i = original.length(); i < finalSize; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Completa uma String à esquerda de forma que ela fique com um tamanho fixo
	 * definido como parâmetro.
	 * 
	 * @param original
	 *            A Srtring original.
	 * @param finalSize
	 *            O tamanho final que a String deve ter.
	 * @param c
	 *            O caractere que preenche possíveis buracos.
	 * @return A String no tamanho certo, preenchida de acordo com o tamanho.
	 */
	public static String leftFill(String original, int finalSize, char c) {
		StringBuilder sb = new StringBuilder();
		for (int i = original.length(); i < finalSize; i++) {
			sb.append(c);
		}
		sb.append(original);
		return sb.toString();
	}

}

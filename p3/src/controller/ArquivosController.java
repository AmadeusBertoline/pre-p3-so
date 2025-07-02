package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ArquivosController implements IArquivoController {

	@Override
	public void readDir(String path) throws IOException {
	}

	@Override
	public void createFile(String path, String nome) throws IOException {
	}

	@Override
	public void readFile(String path, String nome) throws IOException {
		File arq = new File(path, nome);
		if (!arq.exists() || !arq.isFile()) {
			throw new IOException("Arquivo não encontrado ou inválido.");
		}

		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(arq))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				sb.append(linha.trim());
			}
		}

		String json = sb.toString();
		if (json.startsWith("["))
			json = json.substring(1);
		if (json.endsWith("]"))
			json = json.substring(0, json.length() - 1);

		String[] objetos = json.split("\\},\\{");

		for (int i = 0; i < objetos.length; i++) {
			String obj = objetos[i];
			if (i == 0 && !obj.startsWith("{"))
				obj = "{" + obj;
			if (i == objetos.length - 1 && !obj.endsWith("}"))
				obj = obj + "}";

			String name = extrairValor(obj, "name");
			String webPagesArray = extrairArray(obj, "web_pages");
			String primeiroWebsite = extrairPrimeiroValorArray(webPagesArray);

			System.out.println("Faculdade: " + name);
			System.out.println("Website: " + primeiroWebsite);
			System.out.println("---------------------------");
		}
	}

	@Override
	public void openFile(String path, String nome) throws IOException {
	}

	private String extrairValor(String json, String chave) {
		String busca = "\"" + chave + "\"";
		int idx = json.indexOf(busca);
		if (idx == -1)
			return "";

		int inicio = json.indexOf(":", idx) + 1;
		while (inicio < json.length() && (json.charAt(inicio) == ' ' || json.charAt(inicio) == '\"')) {
			inicio++;
		}

		int fim = inicio;
		while (fim < json.length() && json.charAt(fim) != '\"' && json.charAt(fim) != ',' && json.charAt(fim) != '}') {
			fim++;
		}

		return json.substring(inicio, fim);
	}

	private String extrairArray(String json, String chave) {
		String busca = "\"" + chave + "\"";
		int idx = json.indexOf(busca);
		if (idx == -1)
			return "";

		int inicio = json.indexOf("[", idx);
		int fim = json.indexOf("]", inicio);
		if (inicio == -1 || fim == -1)
			return "";

		return json.substring(inicio + 1, fim);
	}

	private String extrairPrimeiroValorArray(String array) {
		int inicio = array.indexOf("\"");
		int fim = array.indexOf("\"", inicio + 1);
		if (inicio == -1 || fim == -1)
			return "";

		return array.substring(inicio + 1, fim);
	}
}

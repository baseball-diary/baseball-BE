package baseball.baseballDiary.auth.utils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * <p>
 * make URL
 * </p>
 */
@Setter
@Slf4j
public class UrlBuilder {

	private final Map<String, String> params = new LinkedHashMap<>(7);
	private String baseUrl;

	private UrlBuilder() {

	}

	/**
	 * @param baseUrl
	 * @return the new {@code UrlBuilder}
	 */
	public static UrlBuilder fromBaseUrl(String baseUrl) {
		UrlBuilder builder = new UrlBuilder();
		builder.setBaseUrl(baseUrl);
		return builder;
	}

	/**
	 * read only param Map
	 *
	 * @return unmodifiable Map
	 * @since 1.15.0
	 */
	public Map<String, Object> getReadOnlyParams() {
		return Collections.unmodifiableMap(params);
	}

	/**
	 * add param
	 *
	 * @param key
	 * @param value
	 * @return this UrlBuilder
	 */
	public UrlBuilder queryParam(String key, Object value) {
		if (StringUtils.isEmpty(key)) {
			throw new RuntimeException("param doesn't null!");
		}
		String valueAsString = (value != null ? value.toString() : null);
		this.params.put(key, valueAsString);

		return this;
	}

	/**
	 * make url
	 *
	 * @return url
	 */
	public String build() {
		return this.build(false);
	}

	/**
	 * make url
	 *
	 * @param encode
	 * @return url
	 */
	public String build(boolean encode) {
		if (MapUtils.isEmpty(this.params)) {
			return this.baseUrl;
		}
		String baseUrl = StringUtils.appendIfNotContain(this.baseUrl, "?", "&");
		String paramString = parseMapToString(this.params, encode);
		return baseUrl + paramString;
	}

	private String parseMapToString(Map<String, String> params, boolean encode) {
		List<String> paramList = new ArrayList<>();
		forEach(params, (k, v) -> {
			if (v == null) {
				paramList.add(k + "=");
			} else {
				try {
					paramList.add(k + "=" + (encode ? URLEncoder.encode(v, StandardCharsets.UTF_8.displayName()) : v));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage());
				}
			}
		});
		return String.join("&", paramList);
	}

	private <K, V> void forEach(Map<K, V> map, BiConsumer<? super K, ? super V> action) {
		if (MapUtils.isEmpty(map) || action == null) {
			return;
		}
		for (Map.Entry<K, V> entry : map.entrySet()) {
			K k;
			V v;
			try {
				k = entry.getKey();
				v = entry.getValue();
			} catch (IllegalStateException ise) {
				// this usually means the entry is no longer in the map.
				throw new ConcurrentModificationException(ise);
			}
			action.accept(k, v);
		}
	}
}
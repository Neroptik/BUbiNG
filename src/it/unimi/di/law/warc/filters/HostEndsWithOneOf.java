package it.unimi.di.law.warc.filters;

/*
 * Copyright (C) 2004-2013 Paolo Boldi, Massimo Santini, and Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.net.URI;
import java.util.Arrays;
import java.util.Set;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

// RELEASE-STATUS: DIST

/** A filter accepting only URIs whose host part ends (case-insensitively) with one of a given set of suffixes. */
public class HostEndsWithOneOf extends AbstractFilter<URI> {

	/** The splitter used to parse a set of comma separated extensions in an arraylist */
	private static final Splitter SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

	/** The accepted suffixes, downcased. */
	private final String[] suffixes;

	/** Creates a filter that only accepts URLs whose host part ends with one of a given set of suffixes.
	 *
	 * @param suffixes the accepted suffixes.
	 */
	public HostEndsWithOneOf(final String[] suffixes) {
		this.suffixes = new String[suffixes.length];
		for (int i = 0; i < suffixes.length; i++) this.suffixes[i] = suffixes[i].toLowerCase();
	}

	/**
	 * Apply the filter to a given URI
	 *
	 * @param uri the URI to be filtered
	 * @return <code>true</code> if the host part of <code>uri</code> ends with one of the inner suffixes
	 */
	@Override
	public boolean apply(final URI uri) {
		String host = uri.getHost(); // BURL guarantees lower case.
		for (String suffix: suffixes) if (host.endsWith(suffix)) return true;
		return false;
	}

	/**
	 * Get a new <code>HostEndsWithOneOf</code> that will accept only URIs whose host part suffix is one of the given suffixes
	 *
	 * @param spec a String containing the allowed suffixes (separated by ',')
	 * @return a new <code>HostEndsWithOneOf</code> that will accept only URIs whose host suffix is one of the strings specified by <code>spec</code>
	 */
	public static HostEndsWithOneOf valueOf(String spec) {
		return new HostEndsWithOneOf(Iterables.toArray(SPLITTER.split(spec), String.class));
	}

	/**
	 * A string representation of the state of this object, that is just the host suffixes allowed.
	 *
	 * @return the strings used by this object to compare suffixes
	 */
	@Override
	public String toString() {
		return toString((Object[])suffixes);
	}

	/**
	 * Compare this object with a given generic one
	 *
	 * @param x the object to be compared
	 * @return <code>true</code> if <code>x</code> is an instance of <code>HostEndsWithOneOf</code> and the suffixes allowed by <code>x</code> are allowed by this and vice versa
	 */
	@Override
	public boolean equals(Object x) {
		if (x instanceof HostEndsWithOneOf) {
			Set<String> suffixSet = new ObjectOpenHashSet<>(suffixes);
			Set<String> xSuffixSet = new ObjectOpenHashSet<>(((HostEndsWithOneOf)x).suffixes);
			return suffixSet.equals(xSuffixSet);
		}
		else return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(suffixes) ^ HostEndsWithOneOf.class.hashCode();
	}

	@Override
	public Filter<URI> copy() {
		return this;
	}
}

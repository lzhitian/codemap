/*
 * Copyright 2003-2007 Keith Seymour.
 * Copyright 1992-2007 The University of Tennessee. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer listed
 *   in this license in the documentation and/or other materials
 *   provided with the distribution.
 * 
 * - Neither the name of the copyright holders nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * This file was auto-generated by the org.netlib.generate.JavaGenerator
 * program, a part of netlib-java.
 * 
 * @see http://code.google.com/p/netlib-java/
 */
package org.netlib.arpack;

import java.util.logging.Logger;
import org.netlib.util.StringW;
import org.netlib.util.booleanW;
import org.netlib.util.doubleW;
import org.netlib.util.floatW;
import org.netlib.util.intW;
import org.netlib.utils.JNIMethods;

/**
 * ARPACK provider implementation which uses the Java Native Interface to access
 * system netlib libraries.
 *
 * @see http://www.netlib.org/
 * @author Samuel Halliday
 */
final class NativeARPACK extends ARPACK {

	// singleton
	protected static final NativeARPACK INSTANCE = new NativeARPACK();

	// indicates if the JNI loaded OK. If this is false, calls to the native
	// methods will fail with UnsatisfiedLinkError
	protected final boolean isLoaded;

	private NativeARPACK() {
		String libname = JNIMethods.getPortableLibraryName("jniarpack");
		try {
			System.loadLibrary(libname);
		} catch (UnsatisfiedLinkError e) {
			isLoaded = false;
			return;
		}
		isLoaded = true;
	}

	@Override
	public native void dnaupd(intW arg1, String arg2, int arg3, String arg4, int arg5, doubleW arg6, double[] arg7, int arg9, double[] arg10, int arg12, int[] arg13, int[] arg15, double[] arg17, double[] arg19, int arg21, intW arg22);

	@Override
	public native void dneupd(boolean arg1, String arg2, boolean[] arg3, double[] arg5, double[] arg7, double[] arg9, int arg11, double arg12, double arg13, double[] arg14, String arg16, int arg17, String arg18, intW arg19, double arg20, double[] arg21, int arg23, double[] arg24, int arg26, int[] arg27, int[] arg29, double[] arg31, double[] arg33, int arg35, intW arg36);

	@Override
	public native void dsaupd(intW arg1, String arg2, int arg3, String arg4, int arg5, doubleW arg6, double[] arg7, int arg9, double[] arg10, int arg12, int[] arg13, int[] arg15, double[] arg17, double[] arg19, int arg21, intW arg22);

	@Override
	public native void dseupd(boolean arg1, String arg2, boolean[] arg3, double[] arg5, double[] arg7, int arg9, double arg10, String arg11, int arg12, String arg13, intW arg14, double arg15, double[] arg16, int arg18, double[] arg19, int arg21, int[] arg22, int[] arg24, double[] arg26, double[] arg28, int arg30, intW arg31);

	@Override
	public native void snaupd(intW arg1, String arg2, int arg3, String arg4, int arg5, floatW arg6, float[] arg7, int arg9, float[] arg10, int arg12, int[] arg13, int[] arg15, float[] arg17, float[] arg19, int arg21, intW arg22);

	@Override
	public native void sneupd(boolean arg1, String arg2, boolean[] arg3, float[] arg5, float[] arg7, float[] arg9, int arg11, float arg12, float arg13, float[] arg14, String arg16, int arg17, String arg18, intW arg19, float arg20, float[] arg21, int arg23, float[] arg24, int arg26, int[] arg27, int[] arg29, float[] arg31, float[] arg33, int arg35, intW arg36);

	@Override
	public native void ssaupd(intW arg1, String arg2, int arg3, String arg4, int arg5, floatW arg6, float[] arg7, int arg9, float[] arg10, int arg12, int[] arg13, int[] arg15, float[] arg17, float[] arg19, int arg21, intW arg22);

	@Override
	public native void sseupd(boolean arg1, String arg2, boolean[] arg3, float[] arg5, float[] arg7, int arg9, float arg10, String arg11, int arg12, String arg13, intW arg14, float arg15, float[] arg16, int arg18, float[] arg19, int arg21, int[] arg22, int[] arg24, float[] arg26, float[] arg28, int arg30, intW arg31);

}

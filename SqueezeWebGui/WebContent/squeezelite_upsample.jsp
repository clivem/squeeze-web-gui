<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title><s:text name="brand.name" />: <s:text name="squeezelite.upsample.title" /></title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link href="html/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
  <link href="favicon.ico" rel="icon" type="image/x-icon" />
  <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
</head>
<body>

	<jsp:include page="header.jsp" />

	<table>
		<tr>
			<td>
				<jsp:include page="logo_small.jsp" />
			</td>
			<td>
				<h2><s:text name="squeezelite.upsample.header" /></h2>
			</td>
		</tr>
	</table>

<hr />

<h3><s:text name="header.contents" /></h3>
<ul>
  <li><s:a href="#Introduction"><s:text name="header.introduction" /></s:a></li>
  <li><s:a href="#Implementation"><s:text name="header.implementation" /></s:a></li>
  <li><s:a href="#FAQ"><s:text name="header.faq" /></s:a></li>
  <li><s:a href="#Options"><s:text name="header.options" /></s:a>
    <p />
    <ul>
      <li><s:a href="#quality">&lt;quality&gt;</s:a></li>
      <li><s:a href="#flags">&lt;flags&gt;</s:a></li>
      <li><s:a href="#attenuation">&lt;attenuation&gt;</s:a></li>
      <li><s:a href="#precision">&lt;precision&gt;</s:a></li>
      <li><s:a href="#passband_end">&lt;passband_end&gt;</s:a></li>
      <li><s:a href="#stopband_start">&lt;stopband_start&gt;</s:a></li>
      <li><s:a href="#phase_response">&lt;phase_response&gt;</s:a></li>
    </ul>
  </li>
</ul>
<hr />

<h3><span id="Introduction"><s:text name="header.introduction" /></span></h3>

<p>Squeezelite is now capable of upsampling the audio data it is playing using 
<s:a href="http://sourceforge.net/projects/soxr/" target="blank_">the SoX Resampler library</s:a>, libsoxr.
</p>
<p>
It is not enabled by default. It can be enabled by a command line option, &quot;-u&quot;. 
From a user perspective, the "-u" option 
enables the functionality with a set of default options, which can be over-ridden with options from the command line.
</p>
<p>
This page will attempt to document the available settings and provide examples.
</p>
<hr />

<h3><span id="Implementation"><s:text name="header.implementation" /></span></h3>

<p>In a nutshell, Squeezelite either receives raw PCM data from LMS or data in a format which it can decode. eg. flac. 
If the data needs to be decoded to &quot;raw&quot; PCM, it is, before being transferred to the output buffer, which supplies data 
to either Alsa or Portaudio, which communicates with the audio hardware device via the OS.
</p>

<p>If the upsample functionality is enabled, an additional step is introduced between decoding and transfer of data to the 
output buffer. Squeezelite will, by either having opened the audio device, or if the &quot;-r&quot; option
was given on the command line, be aware of the maximum sample rate that the output device is capable of. If upsampling is
enabled, squeezelite will request that libsoxr resample the data at the maximum synchronous sample rate, less than or equal 
to, the maximum sample rate.
</p>

<p>Without going off at too much of a tangent, what this means is that this is currently an oversampling implementation, 
rather than asynchronously resampling everything to the maximum sample rate that the output device is capable of. 
Without heading off into the subjective of why I believe that to be a &quot;good thing&quot;, the bottom line,
from an implementation point of view, is this.
</p>

<p>
If the maximum sample rate that the output device is capable of is 384kHz, multiples of 44.1kHz data, 
(ie. 44.1kHz, 88.2kHz, 176.4kHz), will be upsampled to 352.8kHz, the highest integer multiple of 44.1kHz, less than or 
equal to 384kHz, the maximum sample rate.
</p>

<p>
If the maximum sample rate that the output device is capable of is 384kHz, multiples of 48kHz data, 
(ie. 48kHz, 96kHz, 192kHz), will be upsampled to 384kHz, the highest integer multiple of 48kHz, less than or 
equal to 384kHz, the maximum sample rate.
</p>

<h5>Asynchronous</h5>
<p>
(20130809) Adrian has added asynchronous upsampling capability to squeezelite. (Input will be upsampled to the 
max sample rate of the device, regardless of original sample rate.) Enable using the &quot;X&quot; flag via 
<s:a href="#Options"><s:text name="header.options" /></s:a>.
</p>
<hr />

<h3><span id="FAQ"><s:text name="header.faq" /></span></h3>
<p class="bold">Do I need to upsample?
</p>
<p>The upsampling functionality was introduced at the request of John Swenson, the designer of the Community Squeeze 
audio board. It is not enabled by default. It does not have to be enabled. John requested the functionality so that he 
is able disable the built-in digital filter on the DAC chip he has chosen to use for the Community Squeeze project. 
The interpolation and digital filtering for the Community Squeeze audio DAC board will be provisioned using 
software (libsoxr) in preference to using the on-chip DSP.
</p>
<p>As a general rule of thumb, there is no need to enable the Squeezelite upsampling option for your third-party DAC, 
unless perhaps you you have a NOS DAC and wish to oversample in software.
</p>

<p class="bold">Why Sox?
</p>
<p>Again, without getting bogged down in the what and why, (there is plenty of info you can Google on this subject), not 
all resampling implementations are created equal. SoX is one of the best.
</p>
<hr />

<h3><span id="Options"><s:text name="header.options" /></span></h3>
<p>The Squeezelite &quot;-h&quot; option will display the following help information for the &quot;-u&quot; upsample option.
</p>
<pre>Squeezelite v1.3-dev, Copyright 2012, 2013 Adrian Smith. See -t for license terms
Usage: squeezelite [options] [&lt;server&gt;]
  -u [params]		Upsample, params = &lt;quality&gt;:&lt;flags&gt;:&lt;attenuation&gt;:&lt;precision&gt;:&lt;passband_end&gt;:&lt;stopband_start&gt;:&lt;phase_response&gt;
  			 quality = (v|h|m|l|q)(|L|I|M)(|s), (|X) = async - resample to max rate for device, otherwise resample to max sync rate
  			 flags = num in hex,
  			 attenuation = attenuation in dB to apply (default is -1db if not explicitly set),
  			 precision = number of bits precision (NB. HQ = 20. VHQ = 28),
  			 passband_end = number in percent (0dB pt. bandwidth to preserve. nyquist = 100%),
  			 stopband_start = number in percent (Aliasing/imaging control. &gt; passband_end),
  			 phase_response = 0-100 (0 = minimum / 50 = linear / 100 = maximum)
</pre>
<p>The &quot;-u&quot; option without any supplementary arguments enables a default set of resampling options. 
For most users the defaults will be fine. I would suggest before changing them, at the very least, have a read of 
the sox manual page, pay specific attention to the &quot;rate&quot; option and its arguments.
</p>
<p>The &quot;-u&quot; option, (without any additional arguments), is the equivalent to having specified &quot;-u hL&quot;.
</p>

<h3><span id="quality">&lt;quality&gt;</span></h3>
<p>The first optional argument to &quot;-u&quot; allows the specification of &lt;quality&gt; flags from the command line.</p>

<h5>Quality</h5>
<p>From a not-too-technical perspective, and having read the sox manual page, you will be aware of the &quot;rate&quot; 
quality options. The &quot;v|h|m|l|q&quot; arguments are mutually exclusive. I won't document the &quot;m&quot;, 
&quot;l&quot; or &quot;q&quot;, only the &quot;h&quot;, which is high quality (and the default if 
not specified) and &quot;v&quot;, which is very high quality.
</p>

<h5>Filter (Phase response)</h5>
<p>&quot;L|I|M&quot; are mutually exclusive filter options. The default filter if not specified is linear, &quot;L&quot;. 
The minimum phase filter can be selected with &quot;M&quot; and an intermediate filter selected with &quot;I&quot;. 
</p>

<h5>Filter (Bandwidth)</h5>
<p>The &quot;s&quot; argument to the quality option is the equivalent of having specified the &quot;-s&quot; option to 
the SoX rate effect. ie. it preserves 99% bandwidth with a steep cut-off.
</p>

<h5>Examples</h5>
<p>&quot;-u vLs&quot;, would use very high quality setting, linear phase filter and steep cut-off.
</p>
<p>
&quot;-u hM&quot;, would specify high quality, with the minimum phase filter.
</p>
<p>
&quot;-u hMX&quot;, would specify high quality, with the minimum phase filter, async upsampling to max device rate.
</p>

<h3><span id="flags">&lt;flags&gt;</span></h3>
<p>The second optional argument to &quot;-u&quot; allows the user to specify the following arguments, 
(taken from the soxr.h header file), in hex, on the command line.
</p>
<pre>#define SOXR_ROLLOFF_SMALL     0u  /* &lt;= 0.01 dB */
#define SOXR_ROLLOFF_MEDIUM    1u  /* &lt;= 0.35 dB */
#define SOXR_ROLLOFF_NONE      2u  /* For Chebyshev bandwidth. */

#define SOXR_MAINTAIN_3DB_PT   4u  /* Reserved for internal use. */
#define SOXR_HI_PREC_CLOCK     8u  /* Increase 'irrational' ratio accuracy. */
#define SOXR_DOUBLE_PRECISION 16u  /* Use D.P. calcs even if precision &lt;= 20. */
#define SOXR_VR               32u  /* Experimental, variable-rate resampling. */
</pre>

<h5>Examples</h5>
<p>&quot;-u :2&quot;, would specify SOXR_ROLLOFF_NONE.
<p>NB. In the example above, the option string begining with a &quot;:&quot;. ie. the first option, &lt;quality&gt;, has not 
been user specified, so would default to &quot;hL&quot;. So specifying &quot;-u :2&quot; is the same as having specified 
&quot;-u hL:2&quot;.
</p>

<h3><span id="attenuation">&lt;attenuation&gt;</span></h3>
<p>Internally, data is passed to the SoX resample process as 32 bit integers and output from the SoX resample process as
32 bit integers. Why does this matter? There exists the possibility that integer samples, once resampled may 
be clipped. (ie. exceed the maximum value). By default, if the user does not specify an &quot;attenuation&quot; value, 
it will default to -1db. A value of &quot;0&quot; on the command line. ie. &quot;-u ::0&quot; will disable the default 
-1db attenuation being applied.
</p>
<p>NB. Clipped samples will be logged. Keep an eye on the log file.
</p>

<h5>Examples</h5>
<p>&quot;-u ::6&quot;, specifies to apply -6db (ie. halve volume) prior to the resampling process.
</p>

<h3>Advanced options</h3>
<p>The following options are advanced. They should probably only be used by people who know what they mean and 
what they do.
</p>

<h3><span id="precision">&lt;precision&gt;</span></h3>
<p>The internal 'bit' precision used in the re-sample calculations. (ie. quality.)
</p>

<h5>Examples</h5>
<p>&quot;-u :::28&quot;, specifies 28 bit precision.
</p>

<h3><span id="passband_end">&lt;passband_end&gt;</span></h3>
<p>A percentage value between 0 and 100, where 100 is the Nyquist frequency. (Default if not explicitly set, 91.3.)
</p>

<h5>Examples</h5>
<p>&quot;-u ::::98&quot;, specifies passband ends at 98 percent of Nyquist.
</p>

<h3><span id="stopband_start">&lt;stopband_start&gt;</span></h3>
<p>A percentage value between 0 and 100, where 100 is the Nyquist frequency. (Default if not explicitly set, 100.)
</p>

<h5>Examples</h5>
<p>&quot;-u :::::100&quot;, specifies stopband start at Nyquist.
</p>

<h3><span id="phase_response">&lt;phase_response&gt;</span></h3>
<p>A value between 0-100, where &quot;0&quot; is equivalent to the recipe &quot;M&quot; flag for minimum phase, 
&quot;25&quot; is equivalent to the recipe &quot;I&quot; flag for intermediate phase and &quot;50&quot; is equivalent 
to the recipe &quot;L&quot; flag for linear phase.
</p>

<h5>Examples</h5>
<p>&quot;-u ::::::50&quot;, specifies linear phase.
</p>
<hr />

<h3><span id="Author"><s:text name="header.author" /></span></h3>
<p>This information on this page was authored by 
<s:a href="http://forums.slimdevices.com/member.php?3069-JackOfAll" 
     target="blank_">JackOfAll</s:a>.</p>

<hr />
<jsp:include page="footer.jsp" />

</body>
</html>
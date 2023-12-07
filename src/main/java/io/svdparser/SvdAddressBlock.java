package io.svdparser;


import org.w3c.dom.Element;

public class SvdAddressBlock {
	private Long mOffset;
	private Long mSize;
	private String mUsage;

	public static SvdAddressBlock fromElement(Element el) throws SvdParserException {
		if (!el.getNodeName().equals("addressBlock"))
			throw new SvdParserException("Cannot build an SvdAddressBlock from a " + el.getNodeName() + " node!");

		// Offset
		Element offsetElement = Utils.getSingleFirstOrderChildElementByTagName(el, "offset");
		Long offset = Long.decode(offsetElement.getTextContent());

		// Size
		Element sizeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "size");
		Long size = Long.decode(sizeElement.getTextContent());

		// Usage
		Element usageElement = Utils.getSingleFirstOrderChildElementByTagName(el, "usage");
		String usage = usageElement.getTextContent();

		return new SvdAddressBlock(offset, size, usage);
	}

	private SvdAddressBlock(Long offset, Long size, String usage) {
		mOffset = offset;
		mSize = size;
		mUsage = usage;
	}

	public String getUsage() {
		return mUsage;
	}

	public Long getOffset() {
		return mOffset;
	}

	public Long getSize() {
		return mSize;
	}

	public Long getTotalSize() {
		return mOffset + mSize;
	}
}

import React, { FC, useRef } from 'react';
import { UploadCloud } from 'lucide-react';
import { ContentEditorProps } from '@/types/content';

const ContentEditor: FC<ContentEditorProps> = ({ blocks, onBlocksChange, onImageUpload }) => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const dropZoneRef = useRef<HTMLDivElement>(null);

  const handleFileSelect = async (files: FileList | null) => {
    if (!files) return;
    const imageFiles = Array.from(files).filter(file => file.type.startsWith('image/'));
    for (const file of imageFiles) {
      await onImageUpload(file);
    }
  };

  const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    handleFileSelect(e.dataTransfer.files);
  };

  const handleDragOver = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    if (dropZoneRef.current) {
      dropZoneRef.current.classList.add('border-blue-400');
    }
  };

  const handleDragLeave = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    if (dropZoneRef.current) {
      dropZoneRef.current.classList.remove('border-blue-400');
    }
  };

  // 텍스트 블록만 필터링하여 정렬
  const textBlocks = blocks
  .filter(block => block.type === 'text')
  .sort((a, b) => a.orderNum - b.orderNum);

  // 텍스트 블록의 내용을 하나로 합치기
  const fullContent = textBlocks.map(block => block.content).join('\n\n');

  const handleContentChange = (newContent: string) => {
    const paragraphs = newContent.split('\n\n').filter(text => text.trim());

    // 기존 이미지 블록 유지
    const imageBlocks = blocks.filter(block => block.type === 'image');

    // 텍스트 블록 생성
    const newTextBlocks = paragraphs.map((text, index) => ({
      id: `text-${Date.now()}-${index}`,
      type: 'text' as const,
      content: text,
      orderNum: index * 2
    }));

    onBlocksChange([...newTextBlocks, ...imageBlocks].sort((a, b) => a.orderNum - b.orderNum));
  };

  return (
      <div className="mt-8 space-y-6">
        {/* 설명 입력 영역 */}
        <textarea
            value={fullContent}
            onChange={(e) => handleContentChange(e.target.value)}
            placeholder="컨텐츠에 대한 설명을 입력하세요..."
            className="w-full min-h-[200px] p-4 border border-gray-200 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-300 text-gray-700"
        />

        {/* 이미지 업로드 드롭존 */}
        <div className="relative">
          <input
              type="file"
              ref={fileInputRef}
              className="hidden"
              accept="image/*"
              onChange={(e) => handleFileSelect(e.target.files)}
              multiple
          />

          <div
              ref={dropZoneRef}
              className="w-full min-h-[200px] border-2 border-dashed border-gray-300 rounded-lg flex flex-col items-center justify-center cursor-pointer transition-colors hover:border-blue-400"
              onClick={() => fileInputRef.current?.click()}
              onDrop={handleDrop}
              onDragOver={handleDragOver}
              onDragLeave={handleDragLeave}
          >
            <UploadCloud className="w-12 h-12 text-gray-400 mb-2" />
            <p className="text-sm text-gray-500">썸네일 파일을 드래그앤 드롭 하거나 여기를 클릭하여 업로드</p>
            <p className="text-xs text-gray-400 mt-1">JPEG 또는 PNG 형식</p>
            <button className="mt-4 px-6 py-2 bg-gray-100 text-gray-600 rounded-lg text-sm">
              파일 선택
            </button>
          </div>
        </div>

        {/* 업로드된 이미지 목록 */}
        <div className="grid grid-cols-2 gap-4">
          {blocks
          .filter(block => block.type === 'image')
          .sort((a, b) => a.orderNum - b.orderNum)
          .map((block) => (
              <div key={block.id} className="relative group">
                <img
                    src={block.imageFile?.uploadUrl}
                    alt={block.imageFile?.originalFileName}
                    className="w-full h-48 object-cover rounded-lg"
                />
                <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button
                      onClick={() => {
                        const newBlocks = blocks.filter(b => b.id !== block.id);
                        onBlocksChange(newBlocks);
                      }}
                      className="p-2 bg-black/50 text-white rounded-full hover:bg-black/70"
                  >
                    ✕
                  </button>
                </div>
                <p className="mt-2 text-sm text-gray-500 truncate">
                  {block.imageFile?.originalFileName}
                </p>
              </div>
          ))}
        </div>
      </div>
  );
};

export default ContentEditor;
// components/content/edit/BlockComponent.tsx
import React, { FC } from 'react';
import { Block } from '@/types/content';

interface BlockComponentProps {
  block: Block;
  isActive: boolean;
  onChange: (content: string) => void;
  onImageUpload: (file: File) => void;
  onDelete: () => void;
  onFocus: () => void;
}

const BlockComponent: FC<BlockComponentProps> = ({
                                                   block,
                                                   isActive,
                                                   onChange,
                                                   onImageUpload,
                                                   onDelete,
                                                   onFocus
                                                 }) => {
  const handleFileDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
      onImageUpload(file);
    }
  };

  return (
      <div
          className={`relative group border-2 rounded-lg p-4 ${isActive ? 'border-blue-300' : 'border-gray-200'}`}
          onDrop={handleFileDrop}
          onDragOver={e => e.preventDefault()}
      >
        {block.type === 'text' ? (
            <textarea
                value={block.content}
                onChange={(e) => onChange(e.target.value)}
                onFocus={onFocus}
                placeholder="내용을 입력하세요..."
                className="w-full min-h-[100px] p-4 border border-gray-200 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
        ) : (
            <div className="relative">
              <img
                  src={block.imageFile?.uploadUrl}
                  alt={block.imageFile?.originalFileName}
                  className="w-full rounded-lg"
              />
            </div>
        )}

        <button
            onClick={onDelete}
            className="absolute top-2 right-2 p-2 bg-black/50 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
        >
          ✕
        </button>
      </div>
  );
};

export default BlockComponent;
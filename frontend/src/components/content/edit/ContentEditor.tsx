import { Block, ContentEditorProps } from "@/types/content";
import React, { FC, useRef, useState } from "react";
import { GripVertical, ImagePlus, X } from "lucide-react";

const ContentEditor: FC<ContentEditorProps> = ({ blocks, onBlocksChange, onImageUpload }) => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [draggedBlock, setDraggedBlock] = useState<Block | null>(null);
  const [activeBlockId, setActiveBlockId] = useState<string | null>(null);
  const [cursorPos, setCursorPos] = useState<number>(0);

  const handleDragStart = (e: React.DragEvent, block: Block) => {
    setDraggedBlock(block);
    if (e.target instanceof HTMLElement) {
      const ghost = e.target.cloneNode(true) as HTMLElement;
      ghost.style.opacity = '0.5';
      document.body.appendChild(ghost);
      e.dataTransfer.setDragImage(ghost, 0, 0);
      setTimeout(() => document.body.removeChild(ghost), 0);
    }
  };

  const handleDrop = (e: React.DragEvent, targetBlock: Block) => {
    e.preventDefault();
    if (!draggedBlock || draggedBlock.id === targetBlock.id) return;

    const newBlocks = [...blocks];
    const draggedIdx = newBlocks.findIndex(b => b.id === draggedBlock.id);
    const targetIdx = newBlocks.findIndex(b => b.id === targetBlock.id);

    const [removed] = newBlocks.splice(draggedIdx, 1);
    newBlocks.splice(targetIdx, 0, removed);

    newBlocks.forEach((block, index) => {
      block.orderNum = index;
    });

    onBlocksChange(newBlocks);
    setDraggedBlock(null);
  };

  const handleFileUpload = async (file: File) => {
    try {
      if (!activeBlockId) {
        await onImageUpload(file);
        return;
      }

      const activeBlock = blocks.find(b => b.id === activeBlockId);
      if (!activeBlock) {
        await onImageUpload(file);
        return;
      }

      // 커서가 맨 앞일 때는 현재 블록 앞에 이미지 추가
      const insertIndex = cursorPos === 0 ? activeBlock.orderNum : activeBlock.orderNum + 1;

      // 다른 블록들의 순서 조정
      const adjustedBlocks = blocks.map(block => ({
        ...block,
        orderNum: block.orderNum >= insertIndex ? block.orderNum + 1 : block.orderNum,
      }));

      onBlocksChange(adjustedBlocks);
      await onImageUpload(file);
    } catch (error) {
      console.error('Image upload failed:', error);
    }
  };

  const handleTextSplit = (blockId: string, position: number) => {
    const currentBlock = blocks.find(b => b.id === blockId);
    if (!currentBlock) return;

    const beforeText = currentBlock.content.slice(0, position);
    const afterText = currentBlock.content.slice(position);

    // 새로운 블록들 구성
    const newBlocks = position === 0
        // 커서가 맨 앞일 때: 현재 블록 앞에 새 블록 추가
        ? [
          ...blocks.filter(b => b.id !== blockId),
          {
            id: `text-${Date.now()}`,
            type: 'text' as const,
            content: '',
            orderNum: currentBlock.orderNum
          },
          {
            ...currentBlock,
            orderNum: currentBlock.orderNum + 1
          }
        ]
        // 그 외: 현재 블록을 분할하여 뒤에 새 블록 추가
        : [
          ...blocks.filter(b => b.id !== blockId),
          {
            ...currentBlock,
            content: beforeText,
            orderNum: currentBlock.orderNum
          },
          {
            id: `text-${Date.now()}`,
            type: 'text' as const,
            content: afterText,
            orderNum: currentBlock.orderNum + 1
          }
        ];

    // orderNum 재정렬
    newBlocks
    .sort((a, b) => a.orderNum - b.orderNum)
    .forEach((block, index) => {
      block.orderNum = index;
    });

    onBlocksChange(newBlocks);
  };

  const deleteBlock = (blockId: string) => {
    const newBlocks = blocks.filter(block => block.id !== blockId)
    .map((block, index) => ({
      ...block,
      orderNum: index
    }));
    onBlocksChange(newBlocks);
  };

  const renderBlock = (block: Block) => {
    const isText = block.type === 'text';

    return (
        <div
            key={block.id}
            className="group relative"
            draggable={true}
            onDragStart={(e) => handleDragStart(e, block)}
            onDragOver={(e) => e.preventDefault()}
            onDrop={(e) => handleDrop(e, block)}
        >
          <div className="absolute left-0 top-2 opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-center justify-center">
            <div className="hover:bg-gray-100 rounded p-1 cursor-move">
              <GripVertical className="w-4 h-4 text-gray-400" />
            </div>
          </div>

          {isText ? (
              <div className="pl-6">
            <textarea
                value={block.content}
                onChange={(e) => {
                  const newBlocks = blocks.map(b =>
                      b.id === block.id ? { ...b, content: e.target.value } : b
                  );
                  onBlocksChange(newBlocks);
                }}
                onFocus={() => setActiveBlockId(block.id)}
                onBlur={() => setActiveBlockId(null)}
                onSelect={(e) => {
                  const target = e.target as HTMLTextAreaElement;
                  setCursorPos(target.selectionStart);
                }}
                placeholder="내용을 입력하세요..."
                className="w-full resize-none focus:outline-none text-gray-700 py-1"
                rows={1}
                style={{
                  overflow: 'hidden'
                }}
                onInput={(e) => {
                  const target = e.target as HTMLTextAreaElement;
                  target.style.height = 'auto';
                  target.style.height = `${target.scrollHeight}px`;
                }}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    handleTextSplit(block.id, e.currentTarget.selectionStart);
                  } else if (e.key === 'Backspace' && block.content === '') {
                    e.preventDefault();
                    deleteBlock(block.id);
                  }
                }}
            />
              </div>
          ) : (
              <div className="pl-6 relative mb-4">
                <div className="group/image relative inline-block">
                  <img
                      src={block.imageFile?.uploadUrl}
                      alt={block.imageFile?.originalFileName}
                      className="max-w-[600px] max-h-[400px] object-contain rounded-lg shadow-sm"
                  />
                  <button
                      onClick={() => deleteBlock(block.id)}
                      className="absolute top-2 right-2 p-2 bg-black/50 text-white rounded-full opacity-0 group-hover/image:opacity-100 transition-opacity duration-200 hover:bg-black/70"
                  >
                    <X className="w-4 h-4" />
                  </button>
                </div>
                {block.imageFile?.originalFileName && (
                    <p className="mt-1 text-sm text-gray-500">
                      {block.imageFile.originalFileName}
                    </p>
                )}
              </div>
          )}
        </div>
    );
  };

  const sortedBlocks = [...blocks].sort((a, b) => a.orderNum - b.orderNum);

  const handleEditorDrop = async (e: React.DragEvent) => {
    e.preventDefault();
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
      await handleFileUpload(file);
    }
  };

  return (
      <div className="mt-4">
        <input
            type="file"
            ref={fileInputRef}
            className="hidden"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files?.[0];
              if (file) handleFileUpload(file);
            }}
        />

        <div
            className="border rounded-lg bg-white min-h-[400px] shadow-sm"
            onDragOver={(e) => e.preventDefault()}
            onDrop={handleEditorDrop}
        >
          <div className="px-3 py-2 border-b bg-gray-50">
            <button
                onClick={() => fileInputRef.current?.click()}
                className="px-3 py-1.5 text-sm text-gray-600 hover:bg-white rounded-md transition-all flex items-center gap-2 hover:shadow-sm"
            >
              <ImagePlus className="w-4 h-4" />
              이미지 추가
            </button>
          </div>

          <div className="p-6">
            {sortedBlocks.length > 0 ? (
                <div className="space-y-4">
                  {sortedBlocks.map(renderBlock)}
                </div>
            ) : (
                <div className="relative">
              <textarea
                  placeholder="내용을 입력하세요..."
                  className="w-full resize-none focus:outline-none text-gray-700 py-1"
                  rows={1}
                  style={{
                    overflow: 'hidden'
                  }}
                  onChange={(e) => {
                    if (e.target.value) {
                      onBlocksChange([{
                        id: 'text-1',
                        type: 'text',
                        content: e.target.value,
                        orderNum: 0
                      }]);
                    }
                  }}
              />
                  <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
                    <span className="text-gray-400 text-sm">텍스트를 입력하거나 이미지를 드래그하여 추가하세요</span>
                  </div>
                </div>
            )}
          </div>
        </div>
      </div>
  );
};

export default ContentEditor;
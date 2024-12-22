// components/content/edit/ThumbnailUploader.tsx
import {UploadResponse} from "@/types/content";
import React, {FC, useRef} from "react";
import {UploadCloud} from "lucide-react";

interface ThumbnailUploaderProps {
  file: UploadResponse | null;
  onUpload: (file: File) => Promise<void>;
}

const ThumbnailUploader: FC<ThumbnailUploaderProps> = ({ file, onUpload }) => {
  const inputRef = useRef<HTMLInputElement>(null);

  return (
      <div
          className="bg-white border-2 border-dashed border-gray-200 rounded-lg p-6 flex-1 flex flex-col items-center justify-center cursor-pointer transition-colors hover:border-blue-400"
          onDrop={(e) => {
            e.preventDefault();
            const file = e.dataTransfer.files[0];
            if (file) onUpload(file);
          }}
          onClick={() => inputRef.current?.click()}
          onDragOver={(e) => e.preventDefault()}
      >
        {file ? (
            <div className="flex flex-col items-center">
              <p className="text-gray-700">{file.originalFileName}</p>
              <button
                  onClick={() => inputRef.current?.click()}
                  className="mt-2 text-blue-500 hover:text-blue-600"
              >
                다시 업로드
              </button>
            </div>
        ) : (
            <>
              <UploadCloud className="w-12 h-12 text-gray-400 mb-2" />
              <p className="text-sm text-gray-500">썸네일 파일을 드래그앤 드롭 하거나 여기를 클릭하여 업로드</p>
              <p className="text-xs text-gray-400 mt-1">JPEG 또는 PNG 형식</p>
            </>
        )}
        <input
            type="file"
            ref={inputRef}
            className="hidden"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files?.[0];
              if (file) onUpload(file);
            }}
        />
      </div>
  );
};

export default ThumbnailUploader;
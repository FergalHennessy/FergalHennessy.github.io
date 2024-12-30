Jekyll::Hooks.register :notes, :pre_render do |note|
    puts "Processing Note: #{note['title']}"  # This will print the title of each note

    Dir.mkdir("_tags") unless Dir.exist?("_tags")
    all_existing_tags = Dir.entries("_tags")
    .map { |t| t.match(/(.*).md/) }
    .compact.map { |m| m[1] }

    #puts "all_existing_tags: #{all_existing_tags}"

    tags = note['tags'].reject { |t| t.empty? }
    tags.each do |tag|
    generate_tag_file(tag) if !all_existing_tags.include?(tag)
    end
end


def generate_tag_file(tag)
    # generate tag file
    puts "Generating file for tag: #{tag}"
    File.open("_tags/#{tag}.md", "wb") do |file|
    file << "---\nlayout: tags\ntag-name: #{tag}\n---\n"
    end
    # generate feed file
    #File.open("feeds/#{tag}.xml", "wb") do |file|
    #file << "---\nlayout: feed\ntag-name: #{tag}\n---\n"
    #end
end
